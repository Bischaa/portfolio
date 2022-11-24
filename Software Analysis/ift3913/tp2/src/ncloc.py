import sys
import os
import csv
import re

root_dir = sys.argv[1]
header = ['File path', 'Total non-comment lines']
with open('tp2ncloc.csv', 'w', newline='') as c:
    writer = csv.writer(c)
    writer.writerow(header)
    for subdir, dirs, files in os.walk(root_dir):
        for f in files:
            if f.endswith('.java'):
                print(f)
                path = os.path.join(subdir, f)
                with open(path, 'r') as file:
                    lines = file.readlines()
                    line_count = 0
                    line_index = 0
                    line = ''
                    multiline = False
                    commented = False
                    go_next = True
                    while line_index < len(lines):
                        # read next line
                        if go_next:
                            commented = False
                            line = lines[line_index].strip()
                            line_index += 1
                        else:
                            go_next = True
                        # skip line if empty
                        if not line:
                            continue
                        # check if multiline ends
                        if multiline:
                            if re.match('.*\\*/.*', line, re.IGNORECASE):
                                multiline = False
                                go_next = False
                                line = line.split('*/', 2)[1].strip()
                            continue
                        # skip line if comment
                        if line.startswith('//'):
                            continue
                        # check if multiline begins
                        if re.match('.*/\\*.*', line, re.IGNORECASE):
                            multiline = True
                            commented = True
                            go_next = False
                            line = line.split('/*', 2)[1].strip()
                            continue
                        # increment count if no comment in line
                        if not commented:
                            line_count += 1
                    row = [f, line_count]
                    writer.writerow(row)
            else:
                print('Invalid file: ' + f)
