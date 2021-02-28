#IFT1227 Devoir 3
#Travail par:	Maxime Ton   Matricule : 20143044
#		Pierre-Olivier Tremblay   Matricule: 20049076

.data
error1Msg: .asciiz "Erreur : Le test 1 ne donne pas le bon résultat."
error2Msg: .asciiz "Erreur : Le test 2 ne donne pas le bon résultat."
error3Msg: .asciiz "Erreur : Le test 3 ne donne pas le bon résultat."
error4Msg: .asciiz "Erreur : Le test 4 ne donne pas le bon résultat."
error5Msg: .asciiz "Erreur : Le test 5 ne donne pas le bon résultat"
error6Msg: .asciiz "Erreur : Le test 6 ne donne pas le bon résultat."
error7Msg: .asciiz "Erreur : Le test 7 ne donne pas le bon résultat."
error8Msg: .asciiz "Erreur : Le test 8 ne donne pas le bon résultat."
arrayErrors:
	.word error1Msg,error2Msg,error3Msg,error4Msg,error5Msg,error6Msg,error7Msg,error8Msg

.text
main:
	jal tests	#On teste la fonction divisiblePar9
	
	endProg:
		li $v0, 10	#Terminer le programme
		syscall
	
tests:
	addi $a3, $zero,1	#Initialise le compteur qui va déterminer à quel test on se trouve

	#Test 1, n=1  (Réponse attendue non)
	jal  divisiblePar9
	beq $v0,1, errorMsg	#Vérifie si on obtient le résultat attendue
	
	#Test 2, n=0  (Réponse attendue non, car notre fonction prend en compte tous les facteurs de 9 sauf 0)
	addi $a3,$a3,1		#Incrémente le compteur
	
	addi $a1, $zero, 0
	jal divisiblePar9
	beq $v0,1, errorMsg
	
	#Test 3, n=7  (Réponse attendue non)
	addi $a3,$a3,1		#Incrémente le compteur
	
	addi $a1, $zero, 7
	jal divisiblePar9
	beq $v0,1, errorMsg
	
	#Test 4, n=9  (Réponse attendue oui)
	addi $a3,$a3,1		#Incrémente le compteur
	
	addi $a1, $zero, 9
	jal divisiblePar9
	beq $v0,$zero, errorMsg
	
	#Test 5, n=17  (Réponse attendue non)
	addi $a3,$a3,1		#Incrémente le compteur
	
	addi $a1, $zero, 17
	jal divisiblePar9
	beq $v0,1, errorMsg
	
	#Test 6, n=27  (Réponse attendue oui)
	addi $a3,$a3,1		#Incrémente le compteur
	
	addi $a1, $zero, 27
	jal divisiblePar9
	beq $v0,$zero, errorMsg
	
	#Test 7, n=89  (Réponse attendue non)
	addi $a3,$a3,1		#Incrémente le compteur
	
	addi $a1, $zero, 89
	jal divisiblePar9
	beq $v0,1, errorMsg
	
	#Test 8, n=288  (Réponse attendue oui)
	addi $a3,$a3,1		#Incrémente le compteur
	
	addi $a1, $zero, 288
	jal divisiblePar9
	beq $v0,$zero, errorMsg
	
	j endProg

divisiblePar9:
	if:	#Vérifie si n==9
		addi $t1, $zero, 10
		addi $t2, $zero, 9
		slt $s0, $a1, $t1	#Vérifie si n<10
		beq $s0, $zero, else	#Si n>10 on va à else
		
		beq $a1, $t2, jump	#Si n==9
		
		addi $v0,$zero,0		#n%9!=0
		b jump2
	jump:	
		addi $v0,$zero,1		#n%9==0
		
	jump2:	#Quitte la fonction
		jr $ra
	else:
		addi $t0, $zero, 0	
		while:	#Boucle tant que le nouveau $a1>0
			beq $a1,$zero,exit	#Condition d'arrêt ($a1==0)
			div $a1, $t1		#Test pour le quotient entier et le modulo de $a1
			mflo $s6	#Move from low, quotient de la division (Math.floor(n/10))
			mfhi $s7	#Move from hi, modulo de la division (n%10)
			add $t0, $t0, $s7	#Conserve le modulo en mémoire dans $t0
			addi $a1, $s6, 0	#Modifie $a1 pour le quotient entier déterminé avant
			
			j while			#Prochaine itération
		exit:
			addi $a1, $t0, 0	#Nouveau paramètre $a1 devient le dernier modulo déterminer pour avoir $a1<10
			j divisiblePar9		#Appel la fonction de nouveau

errorMsg:	#Pour afficher un message d'erreur

	addi $s0, $zero, 4	#Pour effectuer les sauts de mots
	subi $a3,$a3,1		#Pour obtenir l'indice voulu
	mult $s0, $a3
	mflo $s1		#Contient le résultat de la multiplication
	lw $s2, arrayErrors($s1)	#Message recherché

	li $v0, 4
	la $a0, ($s2)
	syscall
	
	j endProg	#Fin du programme
