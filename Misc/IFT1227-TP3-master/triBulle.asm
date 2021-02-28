#IFT1227 Devoir 3
#Travail par:	Maxime Ton   Matricule : 20143044
#		Pierre-Olivier Tremblay   Matricule: 20049076

#Segment de la mémoire contenant les données globales
.data
msg: .asciiz "Entrez un nombre: " #Message à afficher
tabSpace: .asciiz "\t"
lineSkip: .asciiz "\n"
errorMsg: .asciiz "Erreur: Vous avez excédé la capacité du tampon!"

#Tampon réservé pour le tableau

buffer: .align 4
	.space 400

#Segment de la mémoire contenant le code
.text
main: #Fonction principale du programme
		addi $a3,$zero,400		#Initialiser $a3 à 400 (capacité du tampon)
		jal saisir 		#On commence par la saisit des nombres
		jal afficher 		#On affiche le tableau lorsque la saisit est terminée
	
		#On fait un saut de ligne pour afficher le tableau trié
		li $v0,4 		#Print un String
		la $a0,lineSkip		#Message à afficher
		syscall
	
		jal triBulle 		#On trie le tableau
		jal afficher 		#On affiche le tableau trié
	
		j endProg 		#Programme terminer
	
	endProg:
		li $v0,10 		#Terminer le programme
		syscall
	
saisir:
		#Indice de notre tableau = $t0
		addi $t0, $zero, 0	#Initialiser le registre $t0
	while:  #Boucle jusqu'à l'atteinte de la condition d'arrêt où dépasse capacité du tampon
		beq $t1, -1, exit	# Condition d'arrêt (input [$t1]=-1)
		beq $t0, $a3, error	#Vérifie si l'on dépasse notre capacité tampon, erreur
		li $v0, 4		#Print un String
		la $a0, msg		#Message à afficher
		syscall
	
		#Prendre l'entrée de l'utilisateur
		li $v0, 5		#Prendre un int entré par l'utilisateur
		syscall
	
		#Sauvegarder le int dans $t1
		addi $t1, $v0, 0
		
		beq $t1, -1, exit	#Vérifier la condition d'arrêt
		
		sw $t1, buffer($t0)
		addi $t0, $t0, 4	#Incrémenter l'indice de 4 octets (taille d'un word)
		
		#Sauvegarder $t0 (taille) dans le registre $a1
		addi $a1, $t0, 0
		
		j while			#Prochaine itération
		
	exit: #Quitter la fonction
		jr $ra
	
	error:	#Message d'erreur et ensuite quitter le programme
		li $v0, 4
		la $a0, errorMsg
		syscall
		j endProg	
	

afficher:  #Fonction pour afficher le tableau
		addi $t0, $zero, 0	#Initialiser $t0 qui va devenir l'indice du tableau
		
	while2:  #Boucle sur les éléments du tableau
		beq $t0, $a1, exit2	#Condition d'arrêt de la boucle (indice=taille du tableau)
		
		lw $t6, buffer($t0)     #Charger l'élément du tableau
		
		addi $t0, $t0, 4	#Incrémenter l'indice de 4 octets (taille d'un word)
		
		#Print l'élément du tableau
		li $v0, 1
		addi $a0, $t6, 0
		syscall
		
		#Espacement de la tabulation
		li $v0, 4
		la $a0, tabSpace
		syscall
		
		j while2		#Prochaine itération
	
	exit2:  #Quitter la fonction
		jr $ra
	
triBulle:
	while3:	#Boucle tant que le tableau n'est pas trié
		addi $t7, $zero, 0	#Initialiser $t7 à 0 (faux) qui sera un vérificateur de si on a échangé des éléments
		addi $t0, $zero, 0	#Initialiser $t0 qui sera l'indice du tableau
		addi $t1, $zero, 4	#Initialiser $t1 qui sera l'indice du tableau + 1
		for:	#Boucle sur les éléments du tableau
			beq $t1, $a1, exitFor	#Condition d'arrêt de la boucle (i+1=taille)
			
			lw $t5, buffer($t0)	#Élément i du tableau
			lw $t6, buffer($t1)	#Élément i+1 du tableau
			
			#Vérification si t[i+1]<t[i], si oui on échange les éléments
			slt $s0, $t6, $t5
			
			beq $s0, $zero, jump	#Vérifie si on doit faire l'échange ou non
			
			addi $t2, $t5, 0	#$t2 (temp) est un registre temporaire pour effectuer l'échange
			addi $t5, $t6, 0	# t[i] = t[i+1]
			addi $t6, $t2, 0	# t[i+1] = temp
			addi $t7, $zero, 1	#$t7 = 1 (vrai) (On a échangé des éléments)
		
			sw $t5, buffer($t0) 	#Sauvegarde des nouveau t[i] et t[i+1]
			sw $t6, buffer($t1)
			
		jump:	#Après échange des éléments 
			addi $t0, $t0, 4	#Incrémenter nos indices
			addi $t1, $t1, 4
			
			j for			#Prochaine itération de for
		
		exitFor:
			beq $t7, 0, exit3	#On boucle tant que l'on a effectué un échange
			j while3		#Prochaine itération de while3 
		
	exit3:
		jr $ra	#Quitter la fonction
