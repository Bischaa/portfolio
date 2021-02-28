#IFT1227 Devoir 3
#Travail par:	Maxime Ton   Matricule : 20143044
#		Pierre-Olivier Tremblay   Matricule: 20049076

#Segment de la m�moire contenant les donn�es globales
.data
msg: .asciiz "Entrez un nombre: " #Message � afficher
tabSpace: .asciiz "\t"
lineSkip: .asciiz "\n"
errorMsg: .asciiz "Erreur: Vous avez exc�d� la capacit� du tampon!"

#Tampon r�serv� pour le tableau

buffer: .align 4
	.space 400

#Segment de la m�moire contenant le code
.text
main: #Fonction principale du programme
		addi $a3,$zero,400		#Initialiser $a3 � 400 (capacit� du tampon)
		jal saisir 		#On commence par la saisit des nombres
		jal afficher 		#On affiche le tableau lorsque la saisit est termin�e
	
		#On fait un saut de ligne pour afficher le tableau tri�
		li $v0,4 		#Print un String
		la $a0,lineSkip		#Message � afficher
		syscall
	
		jal triBulle 		#On trie le tableau
		jal afficher 		#On affiche le tableau tri�
	
		j endProg 		#Programme terminer
	
	endProg:
		li $v0,10 		#Terminer le programme
		syscall
	
saisir:
		#Indice de notre tableau = $t0
		addi $t0, $zero, 0	#Initialiser le registre $t0
	while:  #Boucle jusqu'� l'atteinte de la condition d'arr�t o� d�passe capacit� du tampon
		beq $t1, -1, exit	# Condition d'arr�t (input [$t1]=-1)
		beq $t0, $a3, error	#V�rifie si l'on d�passe notre capacit� tampon, erreur
		li $v0, 4		#Print un String
		la $a0, msg		#Message � afficher
		syscall
	
		#Prendre l'entr�e de l'utilisateur
		li $v0, 5		#Prendre un int entr� par l'utilisateur
		syscall
	
		#Sauvegarder le int dans $t1
		addi $t1, $v0, 0
		
		beq $t1, -1, exit	#V�rifier la condition d'arr�t
		
		sw $t1, buffer($t0)
		addi $t0, $t0, 4	#Incr�menter l'indice de 4 octets (taille d'un word)
		
		#Sauvegarder $t0 (taille) dans le registre $a1
		addi $a1, $t0, 0
		
		j while			#Prochaine it�ration
		
	exit: #Quitter la fonction
		jr $ra
	
	error:	#Message d'erreur et ensuite quitter le programme
		li $v0, 4
		la $a0, errorMsg
		syscall
		j endProg	
	

afficher:  #Fonction pour afficher le tableau
		addi $t0, $zero, 0	#Initialiser $t0 qui va devenir l'indice du tableau
		
	while2:  #Boucle sur les �l�ments du tableau
		beq $t0, $a1, exit2	#Condition d'arr�t de la boucle (indice=taille du tableau)
		
		lw $t6, buffer($t0)     #Charger l'�l�ment du tableau
		
		addi $t0, $t0, 4	#Incr�menter l'indice de 4 octets (taille d'un word)
		
		#Print l'�l�ment du tableau
		li $v0, 1
		addi $a0, $t6, 0
		syscall
		
		#Espacement de la tabulation
		li $v0, 4
		la $a0, tabSpace
		syscall
		
		j while2		#Prochaine it�ration
	
	exit2:  #Quitter la fonction
		jr $ra
	
triBulle:
	while3:	#Boucle tant que le tableau n'est pas tri�
		addi $t7, $zero, 0	#Initialiser $t7 � 0 (faux) qui sera un v�rificateur de si on a �chang� des �l�ments
		addi $t0, $zero, 0	#Initialiser $t0 qui sera l'indice du tableau
		addi $t1, $zero, 4	#Initialiser $t1 qui sera l'indice du tableau + 1
		for:	#Boucle sur les �l�ments du tableau
			beq $t1, $a1, exitFor	#Condition d'arr�t de la boucle (i+1=taille)
			
			lw $t5, buffer($t0)	#�l�ment i du tableau
			lw $t6, buffer($t1)	#�l�ment i+1 du tableau
			
			#V�rification si t[i+1]<t[i], si oui on �change les �l�ments
			slt $s0, $t6, $t5
			
			beq $s0, $zero, jump	#V�rifie si on doit faire l'�change ou non
			
			addi $t2, $t5, 0	#$t2 (temp) est un registre temporaire pour effectuer l'�change
			addi $t5, $t6, 0	# t[i] = t[i+1]
			addi $t6, $t2, 0	# t[i+1] = temp
			addi $t7, $zero, 1	#$t7 = 1 (vrai) (On a �chang� des �l�ments)
		
			sw $t5, buffer($t0) 	#Sauvegarde des nouveau t[i] et t[i+1]
			sw $t6, buffer($t1)
			
		jump:	#Apr�s �change des �l�ments 
			addi $t0, $t0, 4	#Incr�menter nos indices
			addi $t1, $t1, 4
			
			j for			#Prochaine it�ration de for
		
		exitFor:
			beq $t7, 0, exit3	#On boucle tant que l'on a effectu� un �change
			j while3		#Prochaine it�ration de while3 
		
	exit3:
		jr $ra	#Quitter la fonction
