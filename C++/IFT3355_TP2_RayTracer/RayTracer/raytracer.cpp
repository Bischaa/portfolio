#include <cstdio>
#include <cstdlib>
#include <cfloat>
#include <cmath>
#include <algorithm>
#include <string>
#include <fstream>
#include <vector>
#include <iostream>
#include <sstream>
#include <map>
#include <vector>

#include "raytracer.hpp"
#include "image.hpp"


void Raytracer::render(const char *filename, const char *depth_filename,
                       Scene const &scene)
{
    // Alloue les deux images qui seront sauvegardées à la fin du programme.
    Image colorImage(scene.resolution[0], scene.resolution[1]);
    Image depthImage(scene.resolution[0], scene.resolution[1]);
    
    // Crée le zBuffer.
    double *zBuffer = new double[scene.resolution[0] * scene.resolution[1]];
    for(int i = 0; i < scene.resolution[0] * scene.resolution[1]; i++) {
        zBuffer[i] = DBL_MAX;
    }

	// @@@@@@ VOTRE CODE ICI
	// Calculez les paramètres de la caméra pour les rayons. Référez-vous aux slides pour les détails.
	//!!! NOTE UTILE : tan() prend des radians plutot que des degrés. Utilisez deg2rad() pour la conversion.
	//!!! NOTE UTILE : Le plan de vue peut être n'importe où, mais il sera implémenté différement.
	// Vous trouverez des références dans le cours.

	Vector camW = (scene.camera.position - scene.camera.center).normalized(); //Direction z de la caméra
	Vector camU = scene.camera.up.cross(camW).normalized(); //Direction en x de la caméra
	Vector camV = camW.cross(camU); //Direction en y de la caméra

	/*
	Soit (a,b,c) le point central:
	scene.position + alpha * camW = (a,b,c) avec a,b sont n'importe quoi et c = scene.position[2] - zNear
	=> scene.position[2] + alpha*camW[2] = scene.position[2] - zNear => alpha = -zNear/scene.position[2]
	*/

	double alpha = scene.camera.zNear / camW[2];

	Vector centerPOV = scene.camera.position - alpha * camW; //Position central du plan

	double heightPOV = 2 * alpha * tan(0.5 * deg2rad(scene.camera.fovy)); //Hauteur du plan distance de 1
	double widthPOV = heightPOV * scene.camera.aspect; //Largeur du plan
	
	double pixelWidth = widthPOV / scene.resolution[0];  //Largeur d'un pixel selon la caméra (largeur de l'écran/resolution en x [nombre de pixel en largeur])
	double pixelHeight = heightPOV / scene.resolution[1]; //Hauteur d'un pixel delon la caméra

	Vector bottomLeftCornerPOV = centerPOV - 0.5 * heightPOV * camV - 0.5 * widthPOV * camU; //Position du coin gauche en bas du plan
	bottomLeftCornerPOV += 0.5 * pixelWidth * camU + 0.5 * pixelHeight * camV; //Pour centrer sur le pixel du coin gauche en bas

	//Il faut peut-être utiliser le coin gauche en haut

	//Équation du plan de l'image, le W est la normale du plan
	//double D = -1*(Vector(A, B, C).dot(centerPOV));

    // Itère sur tous les pixels de l'image.
    for(int y = 0; y < scene.resolution[1]; y++) {
        for(int x = 0; x < scene.resolution[0]; x++) {

            // Génère le rayon approprié pour ce pixel.
			Ray ray;
			if (scene.objects.empty())
			{
				/*
				Pour nous aider à comprendre:
					Vector(x+0.5,y+0.5,0) représente chaque centre de pixel
					scene.camera.position est la position de la caméra
					Vector(-320,-320,640) est le centre du plan de vue (au centre du carré)
					Le cube est centré à (0,0,1440) pour le cas de base avec des côtés de 320px
				*/
				// Pas d'objet dans la scène --> on rend la scène par défaut.
				// Pour celle-ci, le plan de vue est à z = 640 avec une largeur et une hauteur toute deux à 640 pixels.
				// Normalement c'est: ray = Ray(scene.camera.position, (Vector(-320, -320, 640) + Vector(x + 0.5, y + 0.5, 0) - scene.camera.position).normalized());
				ray = Ray(scene.camera.position, (Vector(-320, -320, 720) + Vector(x + 0.5, y + 0.5, 0) - scene.camera.position).normalized()); //Pour tester afin de mieux comprendre
			}
			else
			{
				// @@@@@@ VOTRE CODE ICI
				// Mettez en place le rayon primaire en utilisant les paramètres de la caméra.
				//!!! NOTE UTILE : tous les rayons dont les coordonnées sont exprimées dans le
				//                 repère monde doivent avoir une direction normalisée.

				//leftCornerPOV est au centre du pixel du coin gauche
				Vector pixel = bottomLeftCornerPOV + pixelWidth * x * camU + pixelHeight * y * camV; //Position du pixel
				ray = Ray(scene.camera.position, (pixel - scene.camera.position).normalized());
				
				//Pour la vérification
				//double t = -(camW.dot(scene.camera.position) + D) / (camW.dot((pixel - scene.camera.position).normalized()));
				//Vector point = scene.camera.position +  t * ((pixel - scene.camera.position).normalized());

				/* //Code utilisation pour la vérification du fonctionnement
				if (point != pixel) {
					std::cout << "Erreur";
				}/*
				if (x == 0 && y == 0) {
					std::cout << "Camera in world:" << scene.camera.position[0] << "," << scene.camera.position[1] << "," << scene.camera.position[2] << std::endl;
					std::cout << "Equation plan:" << A << "," << B << "," << C << "," << D << std::endl;
					std::cout << "Param intersection:" << t << std::endl;
					std::cout <<"Intersection:"<<point[0]<<","<<point[1]<<","<<point[2]<<std::endl;
					std::cout << "Pixel:" << pixel[0] << "," << pixel[1] << "," << pixel[2] << std::endl;
				}
				*/
			}

            // Initialise la profondeur de récursivité du rayon.
            int rayDepth = 0;
           
            // Notre lancer de rayons récursif calculera la couleur et la z-profondeur.
            Vector color;

            // Ceci devrait être la profondeur maximum, correspondant à l'arrière plan.
            // NOTE : Ceci suppose que la direction du rayon est de longueur unitaire (normalisée)
			//        et que l'origine du rayon est à la position de la caméra.
            double depth = scene.camera.zFar;

            // Calcule la valeur du pixel en lançant le rayon dans la scène.
            trace(ray, rayDepth, scene, color, depth);

            // Test de profondeur
            if(depth >= scene.camera.zNear && depth <= scene.camera.zFar && 
                depth < zBuffer[x + y*scene.resolution[0]]) {
                zBuffer[x + y*scene.resolution[0]] = depth;

                // Met à jour la couleur de l'image (et sa profondeur)
                colorImage.setPixel(x, y, color);
                depthImage.setPixel(x, y, (depth-scene.camera.zNear) / 
                                    (scene.camera.zFar-scene.camera.zNear));
            }
        }

		// Affiche les informations de l'étape
		if (y % 100 == 0)
		{
			printf("Row %d pixels finished.\n", y);
		}
    }

	// Sauvegarde l'image
    colorImage.writeBMP(filename);
    depthImage.writeBMP(depth_filename);

	printf("Ray tracing finished with images saved.\n");

    delete[] zBuffer;
}


bool Raytracer::trace(Ray const &ray, 
                 int &rayDepth,
                 Scene const &scene,
                 Vector &outColor, double &depth)
{
    // Incrémente la profondeur du rayon.
    rayDepth++;

    // - itérer sur tous les objets en appelant calling Object::intersect.
    // - ne pas accepter les intersections plus lointaines que la profondeur donnée.
    // - appeler Raytracer::shade avec l'intersection la plus proche.
    // - renvoyer true ssi le rayon intersecte un objet.
	if (scene.objects.empty())
	{
		// Pas d'objet dans la scène --> on rend la scène par défaut :
		// Par défaut, un cube est centré en (0, 0, 1280 + 160) avec une longueur de côté de 320, juste en face de la caméra.
		// Test d'intersection :
		double x = 1280 / ray.direction[2] * ray.direction[0] + ray.origin[0];
		double y = 1280 / ray.direction[2] * ray.direction[1] + ray.origin[1];
		if ((x <= 160) && (x >= -160) && (y <= 160) && (y >= -160))
		{
			// S'il y a intersection :
			Material m; m.emission = Vector(16.0, 0, 0); m.reflect = 0; // seulement pour le matériau par défaut ; vous devrez utiliser le matériau de l'objet intersecté
			Intersection intersection;	// seulement par défaut ; vous devrez passer l'intersection trouvée par l'appel à Object::intersect()
			outColor = shade(ray, rayDepth, intersection, m, scene);
			depth = 1280;	// la profondeur devrait être mise à jour dans la méthode Object::intersect()
		}

		rayDepth--;

		return false;
	}
	else
	{
		// @@@@@@ VOTRE CODE ICI
		// Notez que pour Object::intersect(), le paramètre hit correspond à celui courant.
		// Votre intersect() devrait être implémenté pour exclure toute intersection plus lointaine que hit.depth
		
		// Lancer un rayon dans la scène et calcule ses valeurs de couleur/profondeur.
		// Paramètres :
		//   rayon -- le rayon lancé à travers la scène
		//   rayDepth -- la profondeur de récursion du rayon actuellement lancé
		//   scene -- la scène dans laquelle le rayon est lancé
		//   outColor -- rempli avec la couleur calculée pour le rayon à la sortie
		//   depth -- rempli avec la z-profondeur de la plus proche intersection, si elle existe
		//      NOTE : La profondeur passée avec ce paramètre est traitée comme une limite
		//             supérieure. Les objets plus lointain que cette profondeur doivent être
		//             ignorés.
		// Renvoie true ssi le rayon intersecte un objet de la scène 

		bool intersectionExist = false;

		// Test d'intersection :
		for (size_t i = 0; i < scene.objects.size(); i++) {
			Intersection intersection; //Pour que l'intersection soit reset à chaque objet

			bool currentIntersection = scene.objects[i]->intersect(ray, intersection);
			intersectionExist = currentIntersection || intersectionExist; //Comme ça on a toujours True à partir de la première intersection
			if (currentIntersection) {
				// S'il y a intersection :
				if (intersection.depth < depth && intersection.depth >= scene.camera.zNear) { //Peut être avec vérification intersection >= zNear
					Material m = scene.objects[i]->material;
					outColor = shade(ray, rayDepth, intersection, m, scene);
					depth = intersection.depth; //Mettre à jour avec la nouvelle valeur de l'intersection
				}
			}
		}

		if (intersectionExist) { //Si on a eu une intersection
			return true;
		}
		else if (rayDepth < MAX_RAY_RECURSION) { //Si on n'a pas eu d'intersection, mais que la profondeur du rayon n'a pas atteint le maximum
			return trace(ray, rayDepth, scene, outColor, depth);
			//return trace(Ray(ray.origin + ray.direction, ray.direction), rayDepth, scene, outColor, depth); //Test non concluant, mais je le garde pour l'instant
		}
		else{ //Si le rayon a atteint une profondeur maximale
			rayDepth--;

			return false;
		}

	}
}


Vector Raytracer::shade(Ray const& ray,
	int& rayDepth,
	Intersection const& intersection,
	Material const& material,
	Scene const& scene)
{
	// Calcule l'ombrage pour un point et une normale donnés.
	// Utilise le matériau donné ainsi que les sources de lumières
	// et les autres objets de la scène pour les ombres et réflections.
	// Params:
	// Paramètres :
	//   rayon -- le rayon lancé à travers la scène
	//   rayDepth -- la profondeur de récursivité du rayon actuellement lancé
	//   intersection -- informations à propos de l'intersection rayon-surface
	//   material -- le matériau de l'objet au point d'intersection
	//   scene -- la scène dans laquelle le rayon est lancé
	// Renvoie la couleur calculée.

	// - itérer sur toutes les sources de lumières, calculant les contributions ambiant/diffuse/speculaire
	// - utiliser les rayons d'ombre pour déterminer les ombres
	// - intégrer la contribution de chaque lumière
	// - inclure l'émission du matériau de la surface, s'il y a lieu
	// - appeler Raytracer::trace pour les couleurs de reflection/refraction
	// Ne pas réfléchir/réfracter si la profondeur de récursion maximum du rayon a été atteinte !
	//!!! NOTE UTILE : facteur d'atténuation = 1.0 / (a0 + a1 * d + a2 * d * d)..., la lumière ambiante ne s'atténue pas, ni n'est affectée par les ombres
	//!!! NOTE UTILE : n'acceptez pas les intersection des rayons d'ombre qui sont plus loin que la position de la lumière
	//!!! NOTE UTILE : pour chaque type de rayon, i.e. rayon d'ombre, rayon reflechi, et rayon primaire, les profondeurs maximales sont différentes

	Vector diffuse(0);
	Vector ambient(0);
	Vector specular(0);

	Vector reflectedLight(0);
	Vector transmittedLight(0); //Lumière réfractée ou "transparente"

	//Couleur intrinsèque de la surface S(lambda)
	Vector surfaceColor = material.emission + material.ambient + material.diffuse + material.specular;

	Vector outColor = material.emission; //Couleur de sortie

	//Vecteur vers l'oeil (vecteur E)
	Vector Eye = (scene.camera.position - intersection.position).normalized();

	for (auto lightIter = scene.lights.begin(); lightIter != scene.lights.end(); lightIter++)
	{
		// @@@@@@ VOTRE CODE ICI
		// Calculez l'illumination locale ici, souvenez-vous d'ajouter les lumières ensemble.
		// Testez également les ombres ici, si un point est dans l'ombre, multipliez ses couleurs diffuse et spéculaire par (1 - material.shadow)

		//La direction du rayon est le vector normalisé entre la position de la source de lumière et le point d'intersection 
		Vector lightDir = ((*lightIter).position - intersection.position); //Vecteur L
		double lightDist2 = lightDir.length2(); //Distance au carré entre source et point
		double lightDist = sqrt(lightDist2);
		lightDir.normalize(); //Normalisation du vecteur L

		//Facteur d'atténuation À retravailler (Nous allons l'ignorer pour l'instant)
		//double attenuation = 1.0 /( (*lightIter).attenuation[0] + (*lightIter).attenuation[1] * lightDist + (*lightIter).attenuation[2] * lightDist * lightDist);

		//Rayon d'ombre
		Ray shadowRay(intersection.position + 1e-6 * intersection.normal, lightDir);
		//On regarde si on est dans l'ombre
		if (trace(shadowRay, rayDepth, scene, surfaceColor, lightDist)) { //À revoir si on utilise le paramètre surfaceColor

			ambient[0] = (*lightIter).ambient[0] * material.ambient[0] * surfaceColor[0];
			ambient[1] = (*lightIter).ambient[1] * material.ambient[1] * surfaceColor[1];
			ambient[2] = (*lightIter).ambient[2] * material.ambient[2] * surfaceColor[2];

			outColor += ambient;
			continue; //Pas de contribution diffuse ni spéculaire
		}

		/*
		Avant tout, je pense qu'il y a peut-être un problème avec les valeurs utilisées pour les variables dans les équations
		L_a = lumiere.ambient ; L_l = lumiere.ambient + lumiere.diffuse + lumiere.speculaire
		k_a = material.ambient ; k_d = material.diffuse ; k_s = material.specular
		n = material.shininess ; m = material.reflect
		*/

		//Intensité totale de la lumière
		Vector L_l = (*lightIter).ambient + (*lightIter).diffuse + (*lightIter).specular;

		//Lumière ambiante (ne s'atténue pas, n'est pas affecté par la distance ou la direction de la lumière et elle est considérée une seule fois [Voir les notes])
		ambient[0] = (*lightIter).ambient[0] * material.ambient[0] * surfaceColor[0];
		ambient[1] = (*lightIter).ambient[1] * material.ambient[1] * surfaceColor[1];
		ambient[2] = (*lightIter).ambient[2] * material.ambient[2] * surfaceColor[2];

		//Lumière diffuse
		double nDotL = intersection.normal.dot(lightDir);
		diffuse[0] += L_l[0] * material.diffuse[0] * surfaceColor[0] * nDotL / lightDist2;
		diffuse[1] += L_l[1] * material.diffuse[1] * surfaceColor[1] * nDotL / lightDist2;
		diffuse[2] += L_l[2] * material.diffuse[2] * surfaceColor[2] * nDotL / lightDist2;


		//Lumière spéculaire de Blinn
		//Ici il nous faut: le vecteur H 
		Vector H = (lightDir + Eye).normalized();
		double nDotH_to_n = pow(intersection.normal.dot(H), material.shininess);
		specular[0] += (material.reflect * surfaceColor[0] + (1 - material.reflect)) * (material.specular[0] * L_l[0] * nDotH_to_n) / lightDist2;
		specular[1] += (material.reflect * surfaceColor[1] + (1 - material.reflect)) * (material.specular[0] * L_l[1] * nDotH_to_n) / lightDist2;
		specular[2] += (material.reflect * surfaceColor[2] + (1 - material.reflect)) * (material.specular[0] * L_l[2] * nDotH_to_n) / lightDist2;

		outColor += ambient + diffuse + specular;

		
		if (!(ABS_FLOAT(material.refract) < 1e-6) && (rayDepth < MAX_RAY_RECURSION)) { //Si réfractif
			Vector transmittedColor(0);

			/*Il faut trouver une façon de savoir si on entre ou sort d'un objet, sinon la logique est là*/

			Vector v_i = ray.origin - ray.direction; //Direction inverse du rayon
			double nDotVi = intersection.normal.dot(v_i);
			Vector refractedRayDir = (material.refract * nDotVi - sqrt(1 - pow(material.refract, 2) * (1 - pow(nDotVi, 2)))) * intersection.normal - material.refract * v_i;

			Ray refractedRay((intersection.position - 1e-6 * intersection.normal), refractedRayDir.normalized());
			double refractedDepth = scene.camera.zFar;

			if (trace(refractedRay, rayDepth, scene, transmittedColor, refractedDepth)) {
				transmittedLight += transmittedColor;
			}
		}
		else if (!(ABS_FLOAT(material.transparency) < 1e-6) && (rayDepth < MAX_RAY_RECURSION)) { //Si transparent
			//Ne fonctionne pas encore!!!!!
			Vector transmittedColor(0);

			outColor = (1 - material.transparency) * outColor; //On soustrait la lumière qui passe au travers
			double transmittedDepth = scene.camera.zFar;

			if (trace(Ray((intersection.position - 1e-6 * intersection.normal), ray.direction), rayDepth, scene, transmittedColor, transmittedDepth)) {
				transmittedLight += transmittedColor;
			}

		}
	}

	//Réflexion
	/*Fonctionne peut-être*/
	if ((!(ABS_FLOAT(material.reflect) < 1e-6)) && (rayDepth < MAX_RAY_RECURSION))
	{
		// @@@@@@ VOTRE CODE ICI
		// Calculez la couleur réfléchie en utilisant trace() de manière récursive.

			for (auto lightIter = scene.lights.begin(); lightIter != scene.lights.end(); lightIter++) {
				/*On ignore si ça fonctionne comme il faut pour l'instant*/
				Vector reflectedColor(0); //Partie réfléchie

				Vector v_i = ray.origin - ray.direction; //Direction inverse du rayon
				Vector reflectedRay = 2 * (v_i.dot(intersection.normal) * intersection.normal - v_i);

				Ray reflectedLightRay((intersection.position + 1e-6 * intersection.normal), reflectedRay);
				double reflectedDepth = scene.camera.zFar;

				if (trace(reflectedLightRay, rayDepth, scene, reflectedColor, reflectedDepth)) { //Si réflextion
					reflectedLight += reflectedColor; //C'est peut-être = au lieu de +=
				}
			}
	}
	
	
	return outColor + material.reflect * reflectedLight; // +transmittedLight;
	//return material.emission + ambient + diffuse + specular + material.reflect * reflectedLight;
}