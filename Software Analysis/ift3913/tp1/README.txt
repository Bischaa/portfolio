IFT3913 - TP1 par Maxime Ton (20143044) et Zi Kai Qin (20191254)

Repositoire: https://github.com/zikaiqin/ift3913

DOCUMENTATION:

Compilation: 

Le code est compilé à travers IntelliJ. Les modules et dépendances devraient
déjà pré-réglées, il suffit qu'aller dans Build -> Build Artifacts -> All Artifacts
(ou choisir quel partie que l'on veut compiler). Une fois compilé, le jar se retrouvera
sous tp1/out/artifacts/*_jar.
Dans le cas où les modules ne sont pas pré-réglés, il faudra les régler comme suit:
1. File -> New -> Module from Existing Sources;
2. Naviguer jusqu'à chacun de nos dossier de fichiers java sources 
(Exemple: tp1/src/main/java/jls) et cliquer sur OK;
3. Cliquer Next sur tout dans le pop-up Import Project;
4. Une fois de retour dans IntelliJ, aller dans File -> Project Structure -> Modules;
5. Sélectionner le module (exemple: jls) et aller dans Path -> Compiler Output ->
Use module compile output path -> tp1/out/artifacts/*_jar et cliquer Apply;
6. Toujours dans la fenêtre Project Structure, se diriger dans Artifacts;
7. Si rien n'est présent, cliquer le + -> JAR -> From module with dependencies;
8. Choisir le module concerné, choisir la Main Class (même nom que le module), et 
cliquer copy to the ouput directory and link via manifest. Garder en tête où le fichier
manifest sera. Cliquer OK;
9. Dans la page Artifacts, cliquer droit sur *.jar et choisir Create Directory. Le nommer
META-INF et ensuite OK;
10. Sur la même page, cliquer droit sur le dossier META-INF nouvellement créé, Add Copy Of
-> File. Trouver le fichier MANIFEST.MF et cliquer OK;
11. Finalement cliquer Apply et ensuite OK en bas de la page Project Structure.
On peut maintenant build le jar comme vu plus haut.

Exécution: 

L'exécution du code se fait à travers la ligne de commande. Sur Windows,
simplement ouvrir un terminal de commande, naviguer vers tp1/out/artifacts/*_jar, et
ensuite entrer java -jar *.jar [args].

Utilisation du code:

-jls.jar : Entrer un path vers le directoire voulu en arguments, il sortira sur la ligne
de commande un arbre, ainsi qu'un fichier csv dans le dossier tp1/out/artifacts/jls_jar.
Exemple d'utilisation: >java -jar jls.jar */ckjm-master/src

-nvloc.jar : Entrer un path vers un fichier source java, il sortira le nombre de lignes
de code non-vides sur la ligne de commande.
Exemple d'utilisation: >java -jar nvloc.jar */ckjm-master/src/gr/spinellis/ckjm/ant/CkjmTask.java

-lcsec.jar : Entrer un path absolu vers un directoire général duquel on a créer un 
fichier CSV, suivi du fichier CSV en question avec son chemin relatif (pour simplicité,
mettre le fichier CSV dans tp1/out/artifacts/lcsec_jar). Il sortira des lignes de format
CSV ainsi qu'une valeur de couplage CSEC en ligne de commande.
Exemple d'utilisation: >java -jar lcsec.jar */ckjm-master/src jls.csv (dans ce cas, 
jls.csv se trouve dans tp1/out/artifacts/lcsec_jar)

-egon.jar : Entrer un path vers un directoire et un seuil (en pourcentage). Il sortira
en ligne de commande toutes les classes suspectées divine par dessus le seuil ainsi
qu'un fichier CSV egon.csv dans le dossier tp1/out/artifacts/egon_jar.
Exemple d'utilisation: >java -jar egon.jar */ckjm-master/src 10 (dans ce cas 10 représente
un seuil de 10%).