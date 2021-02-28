/*
Modifier par:
-Maxime Ton : 20143044
-Pierre-Olivier Tremblay: 20049076
*/

TP3.Physics = {
	
	initTree: function (rootNode) {
		
		this.computeTreeMass(rootNode);
		
		var stack = [];
		stack.push(rootNode);
		
		while (stack.length > 0) {
			var currentNode = stack.pop();
			for (var i=0; i<currentNode.childNode.length; i++) {
				stack.push(currentNode.childNode[i]);
			}
			
			currentNode.bp0 = currentNode.p0.clone();
			currentNode.bp1 = currentNode.p1.clone();
			currentNode.rp0 = currentNode.p0.clone();
			currentNode.rp1 = currentNode.p1.clone();
			currentNode.vel = new THREE.Vector3();
			currentNode.strength = currentNode.a0;
		}
	},
	
	computeTreeMass: function (node) {
		var mass = 0;
		
		for (var i=0; i<node.childNode.length; i++) {
			mass += this.computeTreeMass(node.childNode[i]);
		}
		mass += node.a1;
		node.mass = mass;
		
		return mass;
	},
	
	applyForces: function (node, dt, time) {

		if(node.childNode == undefined) {
			return node;
		}
		
		var u = Math.sin(1 * time) * 4;
		u += Math.sin(2.5 * time) * 2;
		u += Math.sin(5 * time) * 0.4;
		
		var v = Math.cos(1 * time + 56485) * 4;
		v += Math.cos(2.5 * time + 56485) * 2;
		v += Math.cos(5 * time + 56485) * 0.4;
		
		// Ajouter le vent
		node.vel.add(new THREE.Vector3(u/Math.sqrt(node.mass), 0, v/Math.sqrt(node.mass)).multiplyScalar(dt));
		// Ajouter la gravite
		node.vel.add(new THREE.Vector3(0, -node.mass, 0).multiplyScalar(dt));
		
		// TODO: Projection du mouvement, force de restitution et amortissement de la velocite

		//Vecteur de direction initial
		var initDirection = new THREE.Vector3().subVectors(node.p1, node.p0).normalize();

		//Nouvelle position de la branche
		var newP1 = node.p1.clone();
		newP1.addScaledVector(node.vel, dt);

		//Nos deux vecteurs normalisés
		var norm2 = new THREE.Vector3().subVectors(newP1, node.p0).normalize();
		//console.log(node.p1);
		var norm1 = new THREE.Vector3().subVectors(node.p1, node.p0).normalize();
		//console.log(node.p1);

		//Matrice de rotation
		let axisAngle = TP3.Geometry.findRotation(norm1, norm2);
		let rotMatrix = new THREE.Matrix4().makeRotationFromQuaternion(new THREE.Quaternion().setFromAxisAngle(axisAngle[0],axisAngle[1]));

		//console.log(rotMatrix);
		//Appliquer la matrice de rotation à p1(t)
		//node.p1.applyMatrix4(rotMatrix);
		//Calculer la vrai velocite
		newP1 = node.p1.clone().applyMatrix4(rotMatrix); //Vrai nouveau p1
		
		node.vel = new THREE.Vector3().subVectors(newP1, node.p1).multiplyScalar(1/dt);
		//Force de restitution

		//Vecteur de direction présent
		let newDirection = new THREE.Vector3().subVectors(newP1, node.p0).normalize();

		//Rotation entre direction initiale et la nouvelle direction
		axisAngle = TP3.Geometry.findRotation(initDirection, newDirection);
		//Matrice de rotation mais dans le sens contraire de l'angle au carré
		rotMatrix = new THREE.Matrix4().makeRotationFromQuaternion(new THREE.Quaternion().setFromAxisAngle(axisAngle[0], -Math.pow(axisAngle[1],2)));
		
		//Créer la vélocité dans le sens contraire (vélocité de restitution)
		let restVel = initDirection.clone().applyMatrix4(rotMatrix).multiplyScalar(node.a0*1000);

		//Ajouter la vélocité de restitution
		node.vel.add(restVel);

		//Amortissement de la velocite
		node.vel.multiplyScalar(0.7);

		//Appliquer notre velocite à nos points
		newP1 = node.p1.clone().addScaledVector(node.vel, dt); //p1(t+dt) = p1(t) + v*dt
		
		norm2 = new THREE.Vector3().subVectors(newP1, node.p0).normalize(); //On peut réutiliser l'ancien norm1

		axisAngle = TP3.Geometry.findRotation(norm1, norm2);
		rotMatrix = new THREE.Matrix4().makeRotationFromQuaternion(new THREE.Quaternion().setFromAxisAngle(axisAngle[0],axisAngle[1]));

		norm1.applyMatrix4(rotMatrix);
		node.p1 = node.p0.clone().addScaledVector(norm1, new THREE.Vector3().subVectors(node.p1, node.p0).length());

		//Nouvelle matrice de rotation entre direction initiale et direction finale
		axisAngle = TP3.Geometry.findRotation(initDirection, new THREE.Vector3().subVectors(node.p1, node.p0));
		rotMatrix = new THREE.Matrix4().makeRotationFromQuaternion(new THREE.Quaternion().setFromAxisAngle(axisAngle[0],axisAngle[1]));
		
		//Propage la transformation sur les enfants s'il y en a
		if(node.childNode!=undefined){
			for(let i=0; i<node.childNode.length; i++){
				let childDir = new THREE.Vector3().subVectors(node.childNode[i].p1, node.childNode[i].p0);
				let childLength = childDir.length();
				childDir.normalize();

				node.childNode[i].p0 = node.p1;
				childDir.applyMatrix4(rotMatrix);
				node.childNode[i].p1 = node.p1.clone().addScaledVector(childDir, childLength);
			}
		}
		// Appel récursif sur les enfants
		for (let i = 0; i < node.childNode.length; i++) {
			this.applyForces(node.childNode[i], dt, time);
		}
	}
	
	
	
}