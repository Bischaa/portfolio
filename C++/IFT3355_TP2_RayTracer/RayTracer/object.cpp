#include "object.hpp"

#include <cmath>
#include <cfloat>
#include <fstream>
#include <sstream>
#include <map>
#include <vector>
#include <iostream>


bool Object::intersect(Ray ray, Intersection &hit) const 
{
    // Assure une valeur correcte pour la coordonnée W de l'origine et de la direction
	// Vous pouvez commentez ces lignes si vous faites très attention à la façon de construire vos rayons.
    ray.origin[3] = 1;
    ray.direction[3] = 0;

    Ray local_ray(i_transform * ray.origin, i_transform * ray.direction);
	//!!! NOTE UTILE : pour calculer la profondeur dans localIntersect(), si l'intersection se passe à
	// ray.origin + ray.direction * t, alors t est la profondeur
	// dans localIntersect(), ou vous aurez une profondeur dans le système de coordonnées local, qui
	// ne pourra pas être comparée aux intersection avec les autres objets.
    if (localIntersect(local_ray, hit)) 
	{
        // Assure la valeur correcte de W.
        hit.position[3] = 1;
        hit.normal[3] = 0;
        
		// Transforme les coordonnées de l'intersection dans le repère global.
        hit.position = transform * hit.position;
        hit.normal = (n_transform * hit.normal).normalized();
        
		return true;
    }

    return false;
}


bool Sphere::localIntersect(Ray const& ray, Intersection& hit) const
{
	// @@@@@@ VOTRE CODE ICI
	// Vous pourriez aussi utiliser des relations géométriques pures plutôt que les
	// outils analytiques présentés dans les slides.
	// Ici, dans le système de coordonées local, la sphère est centrée en (0, 0, 0)
	//
	// NOTE : hit.depth est la profondeur de l'intersection actuellement la plus proche,
	// donc n'acceptez pas les intersections qui occurent plus loin que cette valeur.
	//
	//Rappel : Il peut y avoir 0, 1 ou 2 intersections réelles entre un rayon et une sphère
	//Équation de la sphère : x^2+y^2+z^2=rayon^2
	//On veut remplacer x, y et z par l'origine et la direction du rayon

	/* 
	Voici les conditions pour avoir une intersection valable:
	si 
		intersectionExist < 0 : 0 intersection
		intersectionExist = 0 : 1 intersection => b <= 0 (t > 0 et <=hit.depth)
		intersectionExist >0 : 2 intersections => prend le plus petit entre t0 et t1 (mais faut >0), alors b <= 0 pour que ce soit valable
		
		Donc, dans tous les cas, il faut que b <= 0 (si oui, alors t toujours >0)
	*/

	//Les intersections sont les bonnes (testées avec print + Geogebra), mais mauvais output

	//Exprimer l'équation de l'intersection sous forme de paramètres
	//Maintenant la véritable direction du rayon est le point de direction - origin

	//a = 1 car direction normalisée
	double b = 2 * ray.origin.dot(ray.direction);
	double c = ray.origin.length2() - pow(this->radius, 2);

	if (b > 0) return false; //Condition générale

	//La partie b^2-4ac définira le nombre d'intersections
	double intersectionExist = pow(b, 2) - 4 * c;

	//t = équation quadratique
	double t0;
	double t1;

	if (intersectionExist < 0) {//Aucune intersection
		return false;
	}
	else if (intersectionExist == 0) { //Une intersection
		t0 = -b / 2;

		if (t0 <= hit.depth) { //Vérifie si on est à l'intérieur de la limite externe
			//Mettre à jour les informations de l'intersection
			hit.depth = t0;
			hit.position = ray.origin + t0 * ray.direction;
			hit.normal = hit.position.normalized(); //Car centrée en (0,0,0)
			return true;
		}

		return false;
	} 
	else { //2 intersections (intersectionExists > 0)
		t0 = (-b + sqrt(intersectionExist)) / 2;
		t1 = (-b - sqrt(intersectionExist)) / 2;

		//Ici, t0 toujours > 0, mais t1 peut être < 0
		if (t1 < 0) {
			if (t0 <= hit.depth) { //On a une intersection valide seulement si t0<=hit.depth
				hit.depth = t0;
				hit.position = ray.origin + t0 * ray.direction;
				hit.normal = hit.position.normalized(); //Car la sphère est centrée en (0,0,0)
				return true;
			}
			else return false;
		}
		else if (t0 < t1) { //Ici t1 et t0 sont > 0
			hit.depth = t0;
			hit.position = ray.origin + t0 * ray.direction;
			hit.normal = hit.position.normalized(); //Car la sphère est centrée en (0,0,0)
			return true;
		}
		else { //Sinon, t1 et t0 sont > 0 et t1 < t0
			hit.depth = t1;
			hit.position = ray.origin + t1 * ray.direction;
			hit.normal = hit.position.normalized(); // Car la sphère est centrée en (0,0,0)
			return true;
		}
	}
	
	return false;
}



bool Plane::localIntersect(Ray const &ray, Intersection &hit) const
{
	// @@@@@@ VOTRE CODE ICI
	// N'acceptez pas les intersections tant que le rayon est à l'intérieur du plan.
	// ici, dans le système de coordonées local, le plan est à z = 0.
	//
	// NOTE : hit.depth est la profondeur de l'intersection actuellement la plus proche,
	// donc n'acceptez pas les intersections qui occurent plus loin que cette valeur.
	//
	//Il existe trois cas : le rayon est dans le plan, le rayon est parallèle au plan et le rayon intersecte le plan.
	//De ces trois cas, on souhaite seulement vérifier que le rayon intersecte
	//On a un plan en x,y ce qui signifie que notre normale est (0,0,1) en considérant que l'on regarde dans les z négatifs
	
	Vector normale = Vector(0, 0, 1);

	double t = -(ray.origin.dot(normale)) / ray.direction.dot(normale);

	//t>0 pour être dans le champ de vision
	if(t>0 && t<=hit.depth){
		hit.depth = t;
		hit.position = ray.origin + t * ray.direction;
		hit.normal = normale;
		return true;
	}
	
	return false;
}

// Intersections !
bool Mesh::localIntersect(Ray const &ray, Intersection &hit) const
{
	// Test de la boite englobante
	double tNear = -DBL_MAX, tFar = DBL_MAX;
	for (int i = 0; i < 3; i++) {
		if (ray.direction[i] == 0.0) {
			if (ray.origin[i] < bboxMin[i] || ray.origin[i] > bboxMax[i]) {
				// Rayon parallèle à un plan de la boite englobante et en dehors de la boite
				return false;
			}
			// Rayon parallèle à un plan de la boite et dans la boite: on continue
		}
		else {
			double t1 = (bboxMin[i] - ray.origin[i]) / ray.direction[i];
			double t2 = (bboxMax[i] - ray.origin[i]) / ray.direction[i];
			if (t1 > t2) std::swap(t1, t2); // Assure t1 <= t2

			if (t1 > tNear) tNear = t1; // On veut le plus lointain tNear.
			if (t2 < tFar) tFar = t2; // On veut le plus proche tFar.

			if (tNear > tFar) return false; // Le rayon rate la boite englobante.
			if (tFar < 0) return false; // La boite englobante est derrière le rayon.
		}
	}
	// Si on arrive jusqu'ici, c'est que le rayon a intersecté la boite englobante.

	// Le rayon interesecte la boite englobante, donc on teste chaque triangle.
	bool isHit = false;
	for (size_t tri_i = 0; tri_i < triangles.size(); tri_i++) {
		Triangle const &tri = triangles[tri_i];

		if (intersectTriangle(ray, tri, hit)) {
			isHit = true;
		}
	}
	return isHit;
}

double Mesh::implicitLineEquation(double p_x, double p_y,
	double e1_x, double e1_y,
	double e2_x, double e2_y) const
{
	return (e2_y - e1_y)*(p_x - e1_x) - (e2_x - e1_x)*(p_y - e1_y);
}

bool Mesh::intersectTriangle(Ray const &ray,
	Triangle const &tri,
	Intersection &hit) const
{
	// Extrait chaque position de sommet des données du maillage.
	Vector const &p0 = positions[tri[0].pi];
	Vector const &p1 = positions[tri[1].pi];
	Vector const &p2 = positions[tri[2].pi];

	// @@@@@@ VOTRE CODE ICI
	// Décidez si le rayon intersecte le triangle (p0,p1,p2).
	// Si c'est le cas, remplissez la structure hit avec les informations
	// de l'intersection et renvoyez true.
	// Vous pourriez trouver utile d'utiliser la routine implicitLineEquation()
	// pour calculer le résultat de l'équation de ligne implicite en 2D.
	//
	// NOTE : hit.depth est la profondeur de l'intersection actuellement la plus proche,
	// donc n'acceptez pas les intersections qui occurent plus loin que cette valeur.
	//!!! NOTE UTILE : pour le point d'intersection, sa normale doit satisfaire hit.normal.dot(ray.direction) < 0

	/*
	On peut voir un triangle comme un plan, alors on peut calculer sa normale de surface qui sera la même pour tous les points.
	Ensuite, nous allons déterminer l'intersection entre ce plan et le rayon.
	Pour les étapes de calcul voir la solution Demo5-6 pour la question 9.*/
	
	//Calculer la normale
	Vector triNormal = ((p0-p1).cross(p2-p1)).normalized();
	
	//On veut vérifier que la normale du triangle est dirigé vers l'origine du rayon
	if (triNormal.dot(ray.direction) >= 0) { //Si oui, alors on prend la direction opposée
		triNormal = -1 * triNormal; //Direction opposée
	}

	//Calculer D de l'équation Ax+By+Cz+D=0
	double D = -triNormal.dot(p1);

	//Déterminer l'intersection
	double t = -(triNormal.dot(ray.origin)+D)/triNormal.dot(ray.direction);
	
	if (t > 0 && t <= hit.depth) { //Si on a une intersection intéressante
		//On doit donc vérifier si l'intersection est à l'intérieur du triangle
		
		//Paramètres de l'intersection en attendant (de toute façon si return false les param ne seront pas pris en compte à l'extérieur de la fonction)
		hit.depth = t;
		hit.normal = triNormal;
		hit.position = ray.origin + t * ray.direction;

		//Calculer les vecteurs du triangle
		Vector v0 = p1 - p0; //p0 -> p1
		Vector v1 = p2 - p1; //p1 -> p2
		Vector v2 = p0 - p2; //p2 -> p0

		//Calculer les vecteurs des sommets vers le hit
		Vector s0 = hit.position - p0; //p0 -> hit
		Vector s1 = hit.position - p1; //p1 -> hit
		Vector s2 = hit.position - p2; //p2 -> hit

		//Calculer les produits vectoriels des vecteurs partant des mêmes sommets
		Vector alpha = v0.cross(s0);
		Vector beta = v1.cross(s1);
		Vector gamma = v2.cross(s2);

		//Produits scalaires de ces produits vectoriels
		double alphaBeta = alpha.dot(beta);
		double alphaGamma = alpha.dot(gamma);
		double betaGamma = beta.dot(gamma);

		//Vérifie leurs signes
		if ((alphaBeta > 0 && alphaGamma > 0 && betaGamma > 0) || (alphaBeta < 0 && alphaGamma < 0 && betaGamma < 0)) {
			return true;
		}
	}
	
	return false;
	
}


bool Plane::is_Plane() const {return true;}
bool Sphere::is_Plane() const { return false;}
bool Mesh::is_Plane() const { return false;}