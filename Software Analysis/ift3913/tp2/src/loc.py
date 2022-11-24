import sys
import os
import csv

root_dir = sys.argv[1]
header = ['File path', 'Total lines']
with open('tp2loc.csv', 'w', newline='') as c:
    writer = csv.writer(c)
    writer.writerow(header)
    for subdir, dirs, files in os.walk(root_dir):
        for f in files:
            if f.endswith('.java'):
                print(f)
                path = os.path.join(subdir, f)
                with open(path, 'r') as file:
                    line_count = sum(1 for line in file if line.strip())
                    row = [f, line_count]
                    writer.writerow(row)
            else:
                print('Invalid file: ' + f)
