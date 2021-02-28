//const THREE = require("./js/three.min");

//Travail complété par Maxime Ton (20143044)

// ASSIGNMENT-SPECIFIC API EXTENSION
THREE.Object3D.prototype.setMatrix = function (a) {
  this.matrix = a;
  this.matrix.decompose(this.position, this.quaternion, this.scale);
};

var start = Date.now();
// SETUP RENDERER AND SCENE
var scene = new THREE.Scene();
var renderer = new THREE.WebGLRenderer();
renderer.setClearColor(0xffffff); // white background colour
document.body.appendChild(renderer.domElement);

// SETUP CAMERA
var camera = new THREE.PerspectiveCamera(30, 1, 0.1, 1000); // view angle, aspect ratio, near, far
camera.position.set(10, 5, 10);
camera.lookAt(scene.position);
scene.add(camera);

// SETUP ORBIT CONTROL OF THE CAMERA
var controls = new THREE.OrbitControls(camera);
controls.damping = 0.2;

// ADAPT TO WINDOW RESIZE
function resize() {
  renderer.setSize(window.innerWidth, window.innerHeight);
  camera.aspect = window.innerWidth / window.innerHeight;
  camera.updateProjectionMatrix();
}

window.addEventListener("resize", resize);
resize();

// FLOOR WITH CHECKERBOARD
var floorTexture = new THREE.ImageUtils.loadTexture("images/tile.jpg");
floorTexture.wrapS = floorTexture.wrapT = THREE.MirroredRepeatWrapping;
floorTexture.repeat.set(4, 4);

var floorMaterial = new THREE.MeshBasicMaterial({
  map: floorTexture,
  side: THREE.DoubleSide,
});
var floorGeometry = new THREE.PlaneBufferGeometry(15, 15);
var floor = new THREE.Mesh(floorGeometry, floorMaterial);
floor.rotation.x = Math.PI / 2;
floor.position.y = 0.0;
scene.add(floor);

// TRANSFORMATIONS

function multMat(m1, m2) {
  return new THREE.Matrix4().multiplyMatrices(m1, m2);
}

function inverseMat(m) {
  return new THREE.Matrix4().getInverse(m, true);
}

function idMat4() {
  return new THREE.Matrix4().identity();
}

function translateMat(matrix, x, y, z) {
  //var translatedMat = matrix.element();
  //Apply translation [x, y, z] to @matrix
  // matrix: THREE.Matrix3
  // x, y, z: float

  var translationMatrice = new THREE.Matrix4().set(1,0,0,x,0,1,0,y,0,0,1,z,0,0,0,1);

  return multMat(translationMatrice, matrix);
}

function rotateMat(matrix, angle, axis) {
  // Apply rotation by @angle with respect to @axis to @matrix
  // matrix: THREE.Matrix3
  // angle: float
  // axis: string "x", "y" or "z"
  var rotationMat = new THREE.Matrix4();

  if (axis == "x") {
    rotationMat.set(1,0,0,0,0,Math.cos(angle),-Math.sin(angle),0,0,Math.sin(angle),Math.cos(angle),0,0,0,0,1);
  } else if (axis == "y") {
    rotationMat.set(Math.cos(angle),0,Math.sin(angle),0,0,1,0,0,-Math.sin(angle),0,Math.cos(angle),0,0,0,0,1);
  } else {
    rotationMat.set(Math.cos(angle),-Math.sin(angle),0,0,Math.sin(angle),Math.cos(angle),0,0,0,0,1,0,0,0,0,1);
  }

  return multMat(rotationMat, matrix);
}

function rotateVec3(v, angle, axis) {
  // Apply rotation by @angle with respect to @axis to vector @v
  // v: THREE.Vector3
  // angle: float
  // axis: string "x", "y" or "z"

  var rotationMat = new THREE.Matrix4();

  if (axis == "x") {
      rotationMat.set(1,0,0,0,0,Math.cos(angle),-Math.sin(angle),0,0,Math.sin(angle),Math.cos(angle),0,0,0,0,1);
  } else if (axis == "y") {
      rotationMat.set(Math.cos(angle),0,Math.sin(angle),0,0,1,0,0,-Math.sin(angle),0,Math.cos(angle),0,0,0,0,1);
  } else {
      rotationMat.set(Math.cos(angle),-Math.sin(angle),0,0,Math.sin(angle),Math.cos(angle),0,0,0,0,1,0,0,0,0,1);
  }

  var arrayMat = rotationMat.elements;
  var rotatedVec = new THREE.Vector3(arrayMat[0]*v.x + arrayMat[4]*v.y + arrayMat[8]*v.z + arrayMat[12],
                               arrayMat[1]*v.x + arrayMat[5]*v.y + arrayMat[9]*v.z + arrayMat[13],
                               arrayMat[2]*v.x + arrayMat[6]*v.y + arrayMat[10]*v.z + arrayMat[14]);

  return rotatedVec;
}

function rescaleMat(matrix, x, y, z) {
  // Apply scaling @x, @y and @z to @matrix
  // matrix: THREE.Matrix3
  // x, y, z: float

  var scalingMat = new THREE.Matrix4().set(x,0,0,0,0,y,0,0,0,0,z,0,0,0,0,1);

  return multMat(scalingMat, matrix);
}

class Robot {
  constructor() {
    // Geometry
    this.torsoHeight = 1.5;
    this.torsoRadius = 0.75;
    this.headRadius = 0.32;
    this.armsHeight = 0.8;
    this.armsRadius = 0.3;
    this.forearmsHeight = 0.4;
    this.forearmsRadius = 0.2;

    this.lowerHeight = 5/4 * this.torsoHeight; //Hauteur total du bas du corps (jambe+cuisse)
    this.thighsRadius = this.lowerHeight/6;
    this.legsRadius = this.lowerHeight/6;

    // Animation
    this.walkDirection = new THREE.Vector3(0, 0, 1);

    // Material
    this.material = new THREE.MeshNormalMaterial();

    // Initial pose
    this.initialize();
  }

  initialTorsoMatrix() {
    var initialTorsoMatrix = idMat4();
    initialTorsoMatrix = translateMat(
      initialTorsoMatrix,
      0,
      this.torsoHeight / 2 + this.lowerHeight,
      0
    );

    return initialTorsoMatrix;
  }

  initialHeadMatrix() {
    var initialHeadMatrix = idMat4();
    initialHeadMatrix = translateMat(
      initialHeadMatrix,
      0,
      this.torsoHeight / 2 + this.headRadius,
      0
    );

    return initialHeadMatrix;
  }

  //Arm 1 initial matrix function
  initialArm1Matrix() {
    var initialArmsMatrix = idMat4();
    initialArmsMatrix = translateMat(initialArmsMatrix, this.torsoRadius*2.35, this.armsHeight/3, 0);
    initialArmsMatrix = rescaleMat(initialArmsMatrix, 0.5, 1.5,0.5);

    return initialArmsMatrix;
  }

  //Arm 2 initial matrix function
  initialArm2Matrix() {
    var initialArmsMatrix = idMat4();
    initialArmsMatrix = translateMat(initialArmsMatrix, this.torsoRadius*-2.35, this.armsHeight/3, 0);
    initialArmsMatrix = rescaleMat(initialArmsMatrix, 0.5, 1.5,0.5);

    return initialArmsMatrix;
  }

  //Forearm 1 intial matrix function
  initialForearm1Matrix() {
    var initialForearmsMatrix = idMat4();
    initialForearmsMatrix = translateMat(initialForearmsMatrix, this.torsoRadius*2.35, this.armsHeight*0.95, 0);
    initialForearmsMatrix = rescaleMat(initialForearmsMatrix, 0.5, 1.5,0.5);

    return initialForearmsMatrix;
  }

  //Forearm 2 initial matrix function
  initialForearm2Matrix() {
    var initialForearmsMatrix = idMat4();
    initialForearmsMatrix = translateMat(initialForearmsMatrix, this.torsoRadius*-2.35, this.armsHeight*0.95, 0);
    initialForearmsMatrix = rescaleMat(initialForearmsMatrix, 0.5, 1.5,0.5);

    return initialForearmsMatrix;
  }

  //Initialize thighs matrix
  initialThighsMatrix(leftRight){
    var initialThighMatrix = idMat4();
    if(leftRight == "left"){ //Pour gauche
       initialThighMatrix = translateMat(initialThighMatrix, this.torsoRadius, -2*(this.torsoRadius + this.thighsRadius*1.5)/3, 0);
       initialThighMatrix = rescaleMat(initialThighMatrix, 0.75, 1.5, 0.75);
    } else{ //Pour droit
       initialThighMatrix = translateMat(initialThighMatrix, -this.torsoRadius, -2*(this.torsoRadius + this.thighsRadius*1.5)/3, 0);
       initialThighMatrix = rescaleMat(initialThighMatrix, 0.75, 1.5, 0.75);
    }
    return initialThighMatrix;
  }

  //Initialize legs matrix
  initialLegsMatrix(leftRight){
      var initialLegMatrix = idMat4();
      if(leftRight == "left"){ //Pour gauche
         initialLegMatrix = translateMat(initialLegMatrix,
                                        this.torsoRadius*1.5,
                                        -2*(this.torsoRadius + this.thighsRadius*1.5)/3 - (this.thighsRadius + this.legsRadius),
                                        0
         );
         initialLegMatrix = rescaleMat(initialLegMatrix, 0.5, 1.5, 0.5);
      } else{ //Pour droit
         initialLegMatrix = translateMat(initialLegMatrix,
                                         -this.torsoRadius*1.5,
                                         -2*(this.torsoRadius + this.thighsRadius*1.5)/3 - (this.thighsRadius + this.legsRadius),
                                         0
         );
         initialLegMatrix = rescaleMat(initialLegMatrix, 0.5, 1.5, 0.5);
      }
      return initialLegMatrix;
    }

  initialize() {
    // Torso
    var torsoGeometry = new THREE.CubeGeometry(
      2 * this.torsoRadius,
      this.torsoHeight,
      this.torsoRadius,
      64
    );
    this.torso = new THREE.Mesh(torsoGeometry, this.material);

    // Head
    var headGeometry = new THREE.CubeGeometry(
      2 * this.headRadius,
      this.headRadius,
      this.headRadius
    );
    this.head = new THREE.Mesh(headGeometry, this.material);

    // Arms
    var arm1Geometry = new THREE.SphereGeometry(
        this.armsRadius,
        32,
        32
    );
    this.arm1 = new THREE.Mesh(arm1Geometry, this.material);

    var arm2Geometry = new THREE.SphereGeometry(
        this.armsRadius,
        32,
        32
    );
    this.arm2 = new THREE.Mesh(arm2Geometry, this.material);

    //Forearms
    var forearm1Geometry = new THREE.SphereGeometry(
        this.forearmsRadius,
        32,
        32
    );
    this.forearm1 = new THREE.Mesh(forearm1Geometry, this.material);

    var forearm2Geometry = new THREE.SphereGeometry(
        this.forearmsRadius,
        32,
        32
    );
    this.forearm2 = new THREE.Mesh(forearm2Geometry, this.material);

    //Thighs
    var thighsGeometry = new THREE.SphereGeometry(
        this.thighsRadius,
        32,
        32
    );
    this.leftThigh = new THREE.Mesh(thighsGeometry, this.material);
    this.rightThigh = new THREE.Mesh(thighsGeometry, this.material);

    //Legs
    var legsGeometry = new THREE.SphereGeometry(
        this.legsRadius,
        32,
        32,
    );
    this.leftLeg = new THREE.Mesh(legsGeometry, this.material);
    this.rightLeg = new THREE.Mesh(legsGeometry, this.material);

    //Saved variables for animation later
    this.totalAngle1 = 0; //Pour l'angle de la jambe
    this.halfWalk = true; //Pour si l'animation avance ou recule

    // Torse transformation
    this.torsoInitialMatrix = this.initialTorsoMatrix();
    this.torsoMatrix = idMat4();
    this.torso.setMatrix(this.torsoInitialMatrix);

    // Head transformation
    this.headInitialMatrix = this.initialHeadMatrix();
    this.headMatrix = idMat4();
    var matrix = multMat(this.torsoInitialMatrix, this.headInitialMatrix);
    this.head.setMatrix(matrix);

    // Arms transformation
    this.arm1InitialMatrix = this.initialArm1Matrix();
    this.arm1Matrix = idMat4();
    var matrix2 = multMat(this.torsoInitialMatrix, this.arm1InitialMatrix);
    this.arm1.setMatrix(matrix2);

    this.arm2InitialMatrix = this.initialArm2Matrix();
    this.arm2Matrix = idMat4();
    var matrix3 = multMat(this.torsoInitialMatrix, this.arm2InitialMatrix);
    this.arm2.setMatrix(matrix3);

    //Forearms transformation
    this.forearm1InitialMatrix = this.initialForearm1Matrix();
    this.forearm1Matrix = idMat4();
    var matrix4 = multMat(this.torsoInitialMatrix, this.forearm1InitialMatrix);
    this.forearm1.setMatrix(matrix4);

    this.forearm2InitialMatrix = this.initialForearm2Matrix();
    this.forearm2Matrix = idMat4();
    var matrix5 = multMat(this.torsoInitialMatrix, this.forearm2InitialMatrix);
    this.forearm2.setMatrix(matrix5);

    //Thighs transformation
    this.leftThighInitialMatrix = this.initialThighsMatrix("left");
    this.leftThighMatrix = idMat4();
    var matrix6 = multMat(this.torsoInitialMatrix, this.leftThighInitialMatrix);
    this.leftThigh.setMatrix(matrix6);

    this.rightThighInitialMatrix = this.initialThighsMatrix("right");
    this.rightThighMatrix = idMat4();
    var matrix7 = multMat(this.torsoInitialMatrix, this.rightThighInitialMatrix);
    this.rightThigh.setMatrix(matrix7);

    //Legs transformation
    this.leftLegInitialMatrix = this.initialLegsMatrix("left");
    this.leftLegMatrix = idMat4();
    var matrix8 = multMat(this.torsoInitialMatrix, this.leftLegInitialMatrix);
    this.leftLeg.setMatrix(matrix8);

    this.rightLegInitialMatrix = this.initialLegsMatrix("right");
    this.rightLegMatrix = idMat4();
    var matrix9 = multMat(this.torsoInitialMatrix, this.rightLegInitialMatrix);
    this.rightLeg.setMatrix(matrix9);

    // Add robot to scene
    scene.add(this.torso);
    scene.add(this.head);
    scene.add(this.arm1);
    scene.add(this.arm2);
    scene.add(this.forearm1);
    scene.add(this.forearm2);
    scene.add(this.leftThigh);
    scene.add(this.rightThigh);
    scene.add(this.leftLeg);
    scene.add(this.rightLeg);

  }

  rotateTorso(angle) {
    var torsoMatrix = this.torsoMatrix;

    this.torsoMatrix = idMat4();
    this.torsoMatrix = rotateMat(this.torsoMatrix, angle, "y");
    this.torsoMatrix = multMat(torsoMatrix, this.torsoMatrix);

    var matrix = multMat(this.torsoMatrix, this.torsoInitialMatrix);
    this.torso.setMatrix(matrix);

    //Attaching various body parts to the torso movement
    var matrix2 = multMat(this.headMatrix, this.headInitialMatrix);
    var matrix1 = multMat(matrix, matrix2);
    this.head.setMatrix(matrix1);

    var matrix3 = multMat(this.arm1Matrix, this.arm1InitialMatrix);
    var matrix5 = multMat(matrix, matrix3);
    this.arm1.setMatrix(matrix5);

    var matrix4 = multMat(this.arm2Matrix, this.arm2InitialMatrix);
    var matrix6 = multMat(matrix, matrix4);
    this.arm2.setMatrix(matrix6);

    var matrix7 = multMat(this.forearm1Matrix, this.forearm1InitialMatrix);
    var matrix8 = multMat(matrix, matrix7);
    this.forearm1.setMatrix(matrix8);

    var matrix9 = multMat(this.forearm2Matrix, this.forearm2InitialMatrix);
    var matrix10 = multMat(matrix, matrix9);
    this.forearm2.setMatrix(matrix10);

    //Thighs when torsoRotate
    var matrix11 = multMat(this.leftThighMatrix, this.leftThighInitialMatrix);
    this.leftThigh.setMatrix(multMat(matrix,matrix11));

    var matrix12 = multMat(this.rightThighMatrix, this.rightThighInitialMatrix);
    this.rightThigh.setMatrix(multMat(matrix, matrix12));

    //Legs when torsoRotate
    var matrix13 = multMat(this.leftLegMatrix, this.leftLegInitialMatrix);
    this.leftLeg.setMatrix(multMat(matrix,matrix13));

    var matrix14 = multMat(this.rightLegMatrix, this.rightLegInitialMatrix);
    this.rightLeg.setMatrix(multMat(matrix, matrix14));

    this.walkDirection = rotateVec3(this.walkDirection, angle, "y");
  }

  moveTorso(speed) {
    this.torsoMatrix = translateMat(
      this.torsoMatrix,
      speed * this.walkDirection.x,
      speed * this.walkDirection.y,
      speed * this.walkDirection.z
    );

    var matrix = multMat(this.torsoMatrix, this.torsoInitialMatrix);
    this.torso.setMatrix(matrix);

    //Attaching various body parts to the torso movement
    var matrix2 = multMat(this.headMatrix, this.headInitialMatrix);
    var matrix1 = multMat(matrix, matrix2);
    this.head.setMatrix(matrix1);

    var matrix3 = multMat(this.arm1Matrix, this.arm1InitialMatrix);
    var matrix5 = multMat(matrix, matrix3);
    this.arm1.setMatrix(matrix5);

    var matrix4 = multMat(this.arm2Matrix, this.arm2InitialMatrix);
    var matrix6 = multMat(matrix, matrix4);
    this.arm2.setMatrix(matrix6);

    var matrix7 = multMat(this.forearm1Matrix, this.forearm1InitialMatrix);
    var matrix8 = multMat(matrix, matrix7);
    this.forearm1.setMatrix(matrix8);

    var matrix9 = multMat(this.forearm2Matrix, this.forearm2InitialMatrix);
    var matrix10 = multMat(matrix, matrix9);
    this.forearm2.setMatrix(matrix10);

    //Thighs when torsoMove
    var matrix11 = multMat(this.leftThighMatrix, this.leftThighInitialMatrix);
    this.leftThigh.setMatrix(multMat(matrix,matrix11));

    var matrix12 = multMat(this.rightThighMatrix, this.rightThighInitialMatrix);
    this.rightThigh.setMatrix(multMat(matrix, matrix12));

    //Legs when torsoMove
    var matrix13 = multMat(this.leftLegMatrix, this.leftLegInitialMatrix);
    this.leftLeg.setMatrix(multMat(matrix,matrix13));

    var matrix14 = multMat(this.rightLegMatrix, this.rightLegInitialMatrix);
    this.rightLeg.setMatrix(multMat(matrix, matrix14));

  }

  rotateHead(angle) {
    var headMatrix = this.headMatrix;

    this.headMatrix = idMat4();
    this.headMatrix = rotateMat(this.headMatrix, angle, "y");
    this.headMatrix = multMat(headMatrix, this.headMatrix);

    var matrix = multMat(this.headMatrix, this.headInitialMatrix);
    matrix = multMat(this.torsoMatrix, matrix);
    matrix = multMat(this.torsoInitialMatrix, matrix);
    this.head.setMatrix(matrix);
  }

  rotateArm1(angle)  {
    var arm1Matrix = this.arm1Matrix;

    this.arm1Matrix = idMat4();
    this.arm1Matrix = rotateMat(this.arm1Matrix, angle, "x");
    this.arm1Matrix = multMat(arm1Matrix, this.arm1Matrix);

    var matrix = multMat(this.arm1Matrix, this.arm1InitialMatrix);
    matrix = multMat(this.torsoMatrix, matrix);
    matrix = multMat(this.torsoInitialMatrix, matrix);
    this.arm1.setMatrix(matrix);

    this.rotateForearm1(angle);
  }

  rotateArm2(angle) {
    var arm2Matrix = this.arm2Matrix;

    this.arm2Matrix = idMat4();
    this.arm2Matrix = rotateMat(this.arm2Matrix, angle, "x");
    this.arm2Matrix = multMat(arm2Matrix, this.arm2Matrix);

    var matrix = multMat(this.arm2Matrix, this.arm2InitialMatrix);
    matrix = multMat(this.torsoMatrix, matrix);
    matrix = multMat(this.torsoInitialMatrix, matrix);
    this.arm2.setMatrix(matrix);

    this.rotateForearm2(angle);
  }

  //Rotate forearm1
  rotateForearm1(angle) {
    var rotationMatrix = rotateMat(idMat4(), angle, "x");

    this.forearm1Matrix = multMat(this.forearm1Matrix, rotationMatrix);

    var matrix =  multMat(this.forearm1Matrix, this.forearm1InitialMatrix);
    matrix = multMat(this.torsoMatrix, matrix);
    matrix = multMat(this.torsoInitialMatrix, matrix);
    this.forearm1.setMatrix(matrix);
  }

  //Rotate forearm2
  rotateForearm2(angle) {
    var rotationMatrix = rotateMat(idMat4(), angle, "x");

    this.forearm2Matrix = multMat(this.forearm2Matrix, rotationMatrix);

    var matrix =  multMat(this.forearm2Matrix, this.forearm2InitialMatrix);
    matrix = multMat(this.torsoMatrix, matrix);
    matrix = multMat(this.torsoInitialMatrix, matrix);
    this.forearm2.setMatrix(matrix);
  }

  //Rotate Thighs
  rotateThighs(leftRight, angle) {
    var rotationMatrix = rotateMat(idMat4(), angle, "x");

    if(leftRight == "left"){
        this.leftThighMatrix = multMat(this.leftThighMatrix, rotationMatrix);

        var matrix = multMat(this.leftThighMatrix, this.leftThighInitialMatrix);
        matrix = multMat(this.torsoMatrix, matrix);
        matrix = multMat(this.torsoInitialMatrix, matrix);
        this.leftThigh.setMatrix(matrix);

        this.rotateLegs("left", angle); //Rotation de la jambe gauche
    } else{
        this.rightThighMatrix = multMat(this.rightThighMatrix, rotationMatrix);

        var matrix = multMat(this.rightThighMatrix, this.rightThighInitialMatrix);
        matrix = multMat(this.torsoMatrix, matrix);
        matrix = multMat(this.torsoInitialMatrix, matrix);
        this.rightThigh.setMatrix(matrix);

        this.rotateLegs("right", angle);
    }

  }

  //Rotate Legs
  rotateLegs(leftRight, angle){
    var rotationMatrix = rotateMat(idMat4(), angle, "x");

    if(leftRight == "left"){
        this.leftLegMatrix = multMat(this.leftLegMatrix, rotationMatrix);

        var matrix =  multMat(this.leftLegMatrix, this.leftLegInitialMatrix);
        matrix = multMat(this.torsoMatrix, matrix);
        matrix = multMat(this.torsoInitialMatrix, matrix);
        this.leftLeg.setMatrix(matrix);

    } else{
       this.rightLegMatrix = multMat(this.rightLegMatrix, rotationMatrix);

       var matrix = multMat(this.rightLegMatrix, this.rightLegInitialMatrix);
       matrix = multMat(this.torsoMatrix, matrix);
       matrix = multMat(this.torsoInitialMatrix, matrix);
       this.rightLeg.setMatrix(matrix);
    }
  }

  //Animation
  walkAnimation(speed){
    this.moveTorso(speed);

    speed = Math.abs(speed); //Pour régler le problème lorsque l'utilisateur recule

    if(this.totalAngle1 <= -0.5){

      this.rotateThighs("right", speed);
      this.rotateThighs("left", -speed);
      this.rotateArm1(speed);
      this.rotateArm2(-speed);
      this.totalAngle1 += speed;

      this.halfWalk = true;
    } else if(this.totalAngle1 >= 0.5){

      this.rotateThighs("right", -speed);
      this.rotateThighs("left", speed);
      this.rotateArm1(-speed);
      this.rotateArm2(speed);
      this.totalAngle1 -= speed;

      this.halfWalk = false;

    } else if(this.halfWalk) { //On avance jusqu'à 0.5

      this.rotateThighs("right", speed);
      this.rotateThighs("left", -speed);
      this.rotateArm1(speed);
      this.rotateArm2(-speed);
      this.totalAngle1 += speed;
    } else if(!this.halfWalk){ //On recule jusqu'à -0.5

      this.rotateThighs("right", -speed);
      this.rotateThighs("left", speed);
      this.rotateArm1(-speed);
      this.rotateArm2(speed);
      this.totalAngle1 -= speed;
    }

    this.yWalkAdjust();
  }

  //Méthode pour l'ajustement de la hauteur du robot lors de la marche
  yWalkAdjust(){
    //Trouver le support

    //Positions des jambes par rapport au sol
    var leftLegPos = this.leftLeg.position;
    var rightLegPos = this.rightLeg.position;

    //Appliquer la rotation et trouver la coordonnée en y du centre des jambes
    var leftY = leftLegPos.y;
    var rightY = rightLegPos.y;

    var walkDirectionTemp = this.walkDirection; //Direction de marche temporaire

    if(leftY - rightY < 0){ //leftY plus petit
        var yMove = -leftY + 1.5*this.legsRadius*Math.cos(this.totalAngle1);
    } else{ //rightY plus petit
        var yMove = -rightY + 1.5*this.legsRadius*Math.cos(this.totalAngle1);
    }

    this.walkDirection = new THREE.Vector3(0, yMove, 0);
    this.moveTorso(1);

    this.walkDirection = walkDirectionTemp; //Remet la direction de marche comme elle était
  }
}

var robot = new Robot();

// LISTEN TO KEYBOARD
var keyboard = new THREEx.KeyboardState();

var selectedRobotComponent = 0;
var components = [
  "Torso",
  "Head",
  "Arm 1",
  "Arm 2",
  "Forearm 1",
  "Forearm 2",
  "Left thigh",
  "Right thigh",
  "Left leg",
  "Right leg",
];
var numberComponents = components.length;

function checkKeyboard() {
  // Next element
  if (keyboard.pressed("e")) {
    selectedRobotComponent = selectedRobotComponent + 1;

    if (selectedRobotComponent < 0) {
      selectedRobotComponent = numberComponents - 1;
    }

    if (selectedRobotComponent >= numberComponents) {
      selectedRobotComponent = 0;
    }

    window.alert(components[selectedRobotComponent] + " selected");
  }

  // Previous element
  if (keyboard.pressed("q")) {
    selectedRobotComponent = selectedRobotComponent - 1;

    if (selectedRobotComponent < 0) {
      selectedRobotComponent = numberComponents - 1;
    }

    if (selectedRobotComponent >= numberComponents) {
      selectedRobotComponent = 0;
    }

    window.alert(components[selectedRobotComponent] + " selected");
  }

  // UP
  if (keyboard.pressed("w")) {
    switch (components[selectedRobotComponent]) {
      case "Torso":
        robot.walkAnimation(0.1);
        break;

      case "Head":
        break;

      case "Arm 1":
        robot.rotateArm1(0.1);
        break;
      case "Arm 2":
        robot.rotateArm2(0.1);
      break;

      case "Forearm 1":
        robot.rotateForearm1(0.1);
      break;

      case "Forearm 2":
        robot.rotateForearm2(0.1);
      break;

      case "Left thigh":
        robot.rotateThighs("left", 0.1);
      break;

      case "Right thigh":
        robot.rotateThighs("right", 0.1);
      break;

      case "Left leg":
        robot.rotateLegs("left", 0.1);
      break;

      case "Right leg":
        robot.rotateLegs("right", 0.1);
      break;
    }
  }

  // DOWN
  if (keyboard.pressed("s")) {
    switch (components[selectedRobotComponent]) {
      case "Torso":
        robot.walkAnimation(-0.1);
        break;

      case "Head":
        break;

      case "Arm 1":
        robot.rotateArm1(-0.1);
      break;

      case "Arm 2":
        robot.rotateArm2(-0.1);
      break;

      case "Forearm 1":
        robot.rotateForearm1(-0.1);
      break;

      case "Forearm2":
        robot.rotateForearm2(-0.1);
      break;

      case "Left thigh":
        robot.rotateThighs("left", -0.1);
      break;

      case "Right thigh":
        robot.rotateThighs("right", -0.1);
      break;

      case "Left leg":
        robot.rotateLegs("left", -0.1);
      break;

      case "Right leg":
        robot.rotateLegs("right", -0.1);
      break;
    }
  }

  // LEFT
  if (keyboard.pressed("a")) {
    switch (components[selectedRobotComponent]) {
      case "Torso":
        robot.rotateTorso(0.1);
        break;
      case "Head":
        robot.rotateHead(0.1);
        break;
      case "Arm 1":
        break;
      case "Arm 2":
        break;
    }
  }

  // RIGHT
  if (keyboard.pressed("d")) {
    switch (components[selectedRobotComponent]) {
      case "Torso":
        robot.rotateTorso(-0.1);
        break;
      case "Head":
        robot.rotateHead(-0.1);
        break;
      case "Arm 1":
        break;
      case "Arm 2":
        break;
    }
  }
}

// SETUP UPDATE CALL-BACK
function update() {
  checkKeyboard();
  requestAnimationFrame(update);
  renderer.render(scene, camera);
}

update();
