/*
Modifier par:
-Maxime Ton : 20143044
-Pierre-Olivier Tremblay: 20049076
*/

TP3.Render = {

	drawTreeRough: function (rootNode, scene, alpha, radialDivisions = 8, leavesCutoff = 0.1, leavesDensity = 10, matrix = new THREE.Matrix4(), geometries = new Array(), leafGeometries = new Array()) {

		//Commencer par générer la branche à rootNode
		var length = rootNode.p0.distanceTo(rootNode.p1);

		var branch = new THREE.CylinderBufferGeometry(rootNode.a1, rootNode.a0, length, radialDivisions);

		let axisAngle = TP3.Geometry.findRotation(new THREE.Vector3(0,1,0), new THREE.Vector3().subVectors(rootNode.p1, rootNode.p0));
		let rotMatrix = new THREE.Matrix4().makeRotationFromQuaternion(new THREE.Quaternion().setFromAxisAngle(axisAngle[0],axisAngle[1]));

		branch.applyMatrix4(rotMatrix);
		branch.translate((rootNode.p0.x + rootNode.p1.x)/2, (rootNode.p0.y + rootNode.p1.y)/2, (rootNode.p0.z + rootNode.p1.z)/2);

		geometries.push(branch);

		//Créer les plans dans lesquels seront situés les feuilles
		if(rootNode.a0 < alpha*leavesCutoff){
			//Si la branche n'est pas terminale
			if(rootNode.childNode != undefined) {
				for(let i = 0; i < leavesDensity; i++){

					var leaf = new THREE.PlaneBufferGeometry(alpha, alpha);

					//Random sur la ligne
					var h = Math.random()*alpha;
					//Rotation random
					var theta = Math.random()*Math.PI*2;
					//Random entre la ligne et la circomférence
					var r = Math.random()*alpha/2;

					//Transformer en cartésien
					var point = new THREE.Vector3(r*Math.cos(theta), r*Math.sin(theta), h);

					//Appliquer le random sur notre référentiel (p1)
					var finalPoint = point.add(rootNode.p1);

					//Appliquer une rotation aléatoirement
					leaf.rotateX(Math.random()*Math.PI*2);
					leaf.rotateY(Math.random()*Math.PI*2);
					leaf.rotateZ(Math.random()*Math.PI*2);

					leaf.translate(finalPoint.x, finalPoint.y, finalPoint.z);


					leafGeometries.push(leaf);
				}
			}
			//Si la branche est terminale
			else {
				for(let i = 0; i < leavesDensity; i++){

					var leaf = new THREE.Mesh(new THREE.PlaneBufferGeometry(alpha, alpha), new THREE.MeshPhongMaterial({color: 0x3A5F0B}));

					//Random sur la ligne
					var h = Math.random()*(alpha*2);
					//Rotation random
					var theta = Math.random()*Math.PI*2;
					//Random entre la ligne et la circomférence
					var r = Math.random()*alpha/2;

					//Transformer en cartésien
					var point = new THREE.Vector3(r*Math.cos(theta), r*Math.sin(theta), h);

					//Appliquer le random sur notre référentiel (p1)

					var finalPoint = point.add(rootNode.p1);

					//Appliquer une rotation aléatoirement
					leaf.rotateX(Math.random()*Math.PI*2);
					leaf.rotateY(Math.random()*Math.PI*2);
					leaf.rotateZ(Math.random()*Math.PI*2);

					leaf.translate(finalPoint.x, finalPoint.y, finalPoint.z);


					leafGeometries.push(leaf);
				}
			}
		}

		//Traverser toutes les branches
		if(rootNode.childNode != undefined){
			//console.log(rootNode.childNode);
			for(let i = 0; i<rootNode.childNode.length; i++){
				this.drawTreeRough(rootNode.childNode[i], scene, alpha, radialDivisions, leavesCutoff, leavesDensity, matrix, geometries, leafGeometries);
			}
		}

		if(rootNode.parentNode == undefined){
			//Merge pour les branches
			let treeGeometry = new THREE.CylinderBufferGeometry();
			treeGeometry = THREE.BufferGeometryUtils.mergeBufferGeometries(geometries);
			const tree = new THREE.Mesh(treeGeometry, new THREE.MeshLambertMaterial({color: 0x8B5A2B}));
			scene.add(tree);

			//Pour les feuilles
			let leavesGeometry = new THREE.PlaneBufferGeometry();
			leavesGeometry = THREE.BufferGeometryUtils.mergeBufferGeometries(leafGeometries);
			const leaves = new THREE.Mesh(leavesGeometry, new THREE.MeshPhongMaterial({color: 0x3A5F0B}));
			scene.add(leaves);
		}		
	},

	drawTreeHermite: function (rootNode, scene, alpha, leavesCutoff = 0.1, leavesDensity = 10, matrix = new THREE.Matrix4(), branchGeometries = new Array(), leafGeometries = new Array()) {

		//Notre liste des segments
		let pointsList = rootNode.sections;

		const indexList = []; //Correspondance entre les points et leur indice
		const customVertices = []; //Sommets
		const customIdx = []; //Faces

		const meanIndices = [];
		const topListIndices = [];
		const bottomListIndices = [];

		let currentIdx = 0;
		for (let i=0; i<pointsList.length; i++) {
		  const subIndexList = [];
		  
		  if (i == 0 || i == pointsList.length-1) {
			for (let j=0; j<pointsList[i].length; j++) {
			  customVertices.push(pointsList[i][j].x, pointsList[i][j].y, pointsList[i][j].z);
			  if (i == 0) {
				topListIndices.push(currentIdx);
			  } else {
				bottomListIndices.push(currentIdx);
			  }
			  currentIdx++;
			}
			const meanPoint = TP3.Geometry.meanPoint(pointsList[i]);
			customVertices.push(meanPoint.x, meanPoint.y, meanPoint.z);
			meanIndices.push(currentIdx);
			currentIdx++;
		  }
		  
		  
		  for (let j=0; j<pointsList[i].length; j++) {
			
			customVertices.push(pointsList[i][j].x, pointsList[i][j].y, pointsList[i][j].z);
			subIndexList.push(currentIdx);
			currentIdx++;
		  }
		  indexList.push(subIndexList);
		}


		for (let i=1; i<indexList.length; i++) {
		  for (let j=0; j<indexList[i].length; j++) {
			const topLeft = indexList[i][j];
			const topRight = indexList[i][(j+1)%indexList[i].length];
			const bottomLeft = indexList[i-1][j];
			const bottomRight = indexList[i-1][(j+1)%indexList[i].length];
			
			customIdx.push(topLeft, bottomRight, bottomLeft); //Face 0
			customIdx.push(topLeft, topRight, bottomRight); //Face 1
		  }
		}


		for (let j=0; j<indexList[0].length; j++) {
		  
		  const topLeft = topListIndices[j];
		  const topRight = topListIndices[(j+1)%indexList[0].length];
		  const bottom = meanIndices[0];
		  
		  customIdx.push(topLeft, topRight, bottom); //Face 0
		}
		for (let j=0; j<indexList[indexList.length-1].length; j++) {
		  
		  const topLeft = bottomListIndices[j];
		  const topRight = bottomListIndices[(j+1)%bottomListIndices.length];
		  const bottom = meanIndices[1];
		  
		  customIdx.push(topLeft, bottom, topRight); //Face 0
		}

		//Liste des sommets en float32
		const floatCustomVertices = new Float32Array(customVertices);

		//Créer la branche
		const branchBuffer = new THREE.BufferGeometry();

		branchBuffer.setAttribute('position', new THREE.BufferAttribute(floatCustomVertices, 3));
		branchBuffer.setIndex(customIdx); //Set faces
		branchBuffer.computeVertexNormals();

		//On utilise le même matériel que dans drawTreeRough
		//new THREE.MeshLambertMaterial({color: 0x8B5A2B})
		branchGeometries.push(branchBuffer);

		if(rootNode.a0 < alpha*leavesCutoff){
			//Si la branche n'est pas terminale
			if(rootNode.childNode != undefined) {
				for(let i = 0; i < leavesDensity; i++){

					var leaf = new THREE.PlaneBufferGeometry(alpha, alpha);

					//Random sur la ligne
					var h = Math.random()*alpha;
					//Rotation random
					var theta = Math.random()*Math.PI*2;
					//Random entre la ligne et la circomférence
					var r = Math.random()*alpha/2;

					//Transformer en cartésien
					var point = new THREE.Vector3(r*Math.cos(theta), r*Math.sin(theta), h);

					//Appliquer le random sur notre référentiel (p1)
					var finalPoint = point.add(rootNode.p1);

					//Appliquer une rotation aléatoirement
					leaf.rotateX(Math.random()*Math.PI*2);
					leaf.rotateY(Math.random()*Math.PI*2);
					leaf.rotateZ(Math.random()*Math.PI*2);

					leaf.translate(finalPoint.x, finalPoint.y, finalPoint.z);


					leafGeometries.push(leaf);
				}
			}
			//Si la branche est terminale
			else {
				for(let i = 0; i < leavesDensity; i++){

					var leaf = new THREE.Mesh(new THREE.PlaneBufferGeometry(alpha, alpha), new THREE.MeshPhongMaterial({color: 0x3A5F0B}));

					//Random sur la ligne
					var h = Math.random()*(alpha*2);
					//Rotation random
					var theta = Math.random()*Math.PI*2;
					//Random entre la ligne et la circomférence
					var r = Math.random()*alpha/2;

					//Transformer en cartésien
					var point = new THREE.Vector3(r*Math.cos(theta), r*Math.sin(theta), h);

					//Appliquer le random sur notre référentiel (p1)

					var finalPoint = point.add(rootNode.p1);

					//Appliquer une rotation aléatoirement
					leaf.rotateX(Math.random()*Math.PI*2);
					leaf.rotateY(Math.random()*Math.PI*2);
					leaf.rotateZ(Math.random()*Math.PI*2);

					leaf.translate(finalPoint.x, finalPoint.y, finalPoint.z);


					leafGeometries.push(leaf);
				}
			}
		}

		//Traverser toutes les branches
		if(rootNode.childNode != undefined){
			//console.log(rootNode.childNode);
			for(let i = 0; i<rootNode.childNode.length; i++){
				this.drawTreeHermite(rootNode.childNode[i], scene, alpha, leavesCutoff, leavesDensity, matrix, branchGeometries, leafGeometries);
			}
		}

		if(rootNode.parentNode == undefined){
			//Merge pour les branches
			let treeGeometry = new THREE.CylinderBufferGeometry();
			treeGeometry = THREE.BufferGeometryUtils.mergeBufferGeometries(branchGeometries);
			const tree = new THREE.Mesh(treeGeometry, new THREE.MeshLambertMaterial({color: 0x8B5A2B}));
			scene.add(tree);

			//Pour les feuilles
			let leavesGeometry = new THREE.PlaneBufferGeometry();
			leavesGeometry = THREE.BufferGeometryUtils.mergeBufferGeometries(leafGeometries);
			const leaves = new THREE.Mesh(leavesGeometry, new THREE.MeshPhongMaterial({color: 0x3A5F0B}));
			scene.add(leaves);
		}	
	},
	
	updateTreeHermite: function (trunkGeometryBuffer, leavesGeometryBuffer, rootNode) {
		/*
		On doit trouver une façon de sauvegarder la matrice de déplacement dans le noeud pour modifier p0 et p1.
		Ensuite, on doit appliquer cette matrice sur les sommets correspondants dans trunkGeometryBuffer et leavesGeometryBuffer.

		Mais on doit réussir à modifier generateSegmentsHermite afin de déterminer les sommets correspondants
		*/
	},
	
	drawTreeSkeleton: function (rootNode, scene, color = 0xffffff, matrix = new THREE.Matrix4()) {
		
		var stack = [];
		stack.push(rootNode);
			
		var points = [];
		
		while (stack.length > 0) {
			var currentNode = stack.pop();
			
			for (var i=0; i<currentNode.childNode.length; i++) {
				stack.push(currentNode.childNode[i]);
			}
			
			points.push(currentNode.p0);
			points.push(currentNode.p1);
			
		}
		
		var geometry = new THREE.BufferGeometry().setFromPoints(points);
		var material = new THREE.LineBasicMaterial({color: color});
		var line = new THREE.LineSegments(geometry, material);
		line.applyMatrix4(matrix);
		scene.add(line);
		
		return line.geometry;
	},
	
	updateTreeSkeleton: function (geometryBuffer, rootNode) {
		
		var stack = [];
		stack.push(rootNode);
		
		var idx = 0;
		while (stack.length > 0) {
			var currentNode = stack.pop();
			
			for (var i=0; i<currentNode.childNode.length; i++) {
				stack.push(currentNode.childNode[i]);
			}
			geometryBuffer[idx * 6] = currentNode.p0.x;
			geometryBuffer[idx * 6 + 1] = currentNode.p0.y;
			geometryBuffer[idx * 6 + 2] = currentNode.p0.z;
			geometryBuffer[idx * 6 + 3] = currentNode.p1.x;
			geometryBuffer[idx * 6 + 4] = currentNode.p1.y;
			geometryBuffer[idx * 6 + 5] = currentNode.p1.z;
			
			idx++;
		}
	},
	
	
	drawTreeNodes: function (rootNode, scene, color = 0x00ff00, size = 0.05, matrix = new THREE.Matrix4()) {
		
		var stack = [];
		stack.push(rootNode);
			
		var points = [];
		
		while (stack.length > 0) {
			var currentNode = stack.pop();
			
			for (var i=0; i<currentNode.childNode.length; i++) {
				stack.push(currentNode.childNode[i]);
			}
			
			points.push(currentNode.p0);
			points.push(currentNode.p1);
			
		}
		
		var geometry = new THREE.BufferGeometry().setFromPoints(points);
		var material = new THREE.PointsMaterial({color: color, size: size});
		var points = new THREE.Points(geometry, material);
		points.applyMatrix4(matrix);
		scene.add(points);
		
	},
	
	
	drawTreeSegments: function (rootNode, scene, lineColor = 0xff0000, segmentColor = 0xffffff, orientationColor = 0x00ff00, matrix = new THREE.Matrix4()) {
		
		var stack = [];
		stack.push(rootNode);
			
		var points = [];
		var pointsS = [];
		var pointsT = [];
		
		while (stack.length > 0) {
			var currentNode = stack.pop();
			
			for (var i=0; i<currentNode.childNode.length; i++) {
				stack.push(currentNode.childNode[i]);
			}
			
			const segments = currentNode.sections;
			for (var i=0; i<segments.length-1; i++) {
				points.push(TP3.Geometry.meanPoint(segments[i]));
				points.push(TP3.Geometry.meanPoint(segments[i+1]));
			}
			for (var i=0; i<segments.length; i++) {
				pointsT.push(TP3.Geometry.meanPoint(segments[i]));
				pointsT.push(segments[i][0]);
			}
			
			for (var i=0; i<segments.length; i++) {
				
				for (var j=0; j<segments[i].length-1; j++) {
					pointsS.push(segments[i][j]);
					pointsS.push(segments[i][j+1]);
				}
				pointsS.push(segments[i][0]);
				pointsS.push(segments[i][segments[i].length-1]);
			}
		}
		
		var geometry = new THREE.BufferGeometry().setFromPoints(points);
		var geometryS = new THREE.BufferGeometry().setFromPoints(pointsS);
		var geometryT = new THREE.BufferGeometry().setFromPoints(pointsT);

		var material = new THREE.LineBasicMaterial({color: lineColor});
		var materialS = new THREE.LineBasicMaterial({color: segmentColor});
		var materialT = new THREE.LineBasicMaterial({color: orientationColor});
		
		var line = new THREE.LineSegments(geometry, material);
		var lineS = new THREE.LineSegments(geometryS, materialS);
		var lineT = new THREE.LineSegments(geometryT, materialT);
		
		line.applyMatrix4(matrix);
		lineS.applyMatrix4(matrix);
		lineT.applyMatrix4(matrix);
		
		scene.add(line);
		scene.add(lineS);
		scene.add(lineT);
		
	}
}