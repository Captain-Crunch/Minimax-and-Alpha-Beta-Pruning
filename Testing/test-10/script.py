import os
import filecmp
import sys

num = 1
max = 11
passed = 0
failed = 0
xcept = 0
in_ext = ".txt"
out_ext = ".txt"
os.system("javac homework.java")
gold_path = "OUTPUT\\"
input_path = "INPUT\\"
while num < max:

	try:
		l1 = list()
		l2 = list()
		
		fnum = str(num).zfill(2)
		#fnum = str(num)
		ip = input_path + "input" + fnum + in_ext
		op = "output" + fnum + out_ext
		os.system("java homework " + ip + " " + op)
		
		with open(op, 'r') as f1:
			l1 = f1.read().splitlines()
		
		with open(gold_path+op, 'r') as f2:
			l2 = f2.read().splitlines()
		
		print("Your content: ")
		print(l1)
		print()
		
		print("Gold Standard: ")
		print(l2)
		print()
		
		if l1 == l2:
			print("Test Case: ", num, " PASSED")
			passed += 1
			print()
		else:
			print("Test Case: ", num, " FAILED")
			failed += 1
			print()
			#sys.exit(1)
		
	except Exception:
		print("Test Case: ", num, " EXCEPTION")
		xcept += 1
		print()
		pass
	num += 1

print("Number of Cases PASSED: \n", passed)
print("Number of Cases FAILED: \n", failed)
print("Number of EXCEPTIONS: \n", xcept)
print("TOTAL Number of Cases: \n", num)