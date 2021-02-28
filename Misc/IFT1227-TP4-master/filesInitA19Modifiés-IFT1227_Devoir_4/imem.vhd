library IEEE;
use IEEE.STD_LOGIC_1164.all; 
use STD.TEXTIO.all;
use IEEE.STD_LOGIC_UNSIGNED.all; use IEEE.STD_LOGIC_ARITH.all;

entity imem is -- instruction memory, TP4 H19
	port (	a: 	in STD_LOGIC_VECTOR (5 downto 0);
			rd: out STD_LOGIC_VECTOR (31 downto 0));
end;

architecture behave of imem is
begin
	process(a)
		type ramtype is array (63 downto 0) of STD_LOGIC_VECTOR(31 downto 0);
		variable mem: ramtype;
	begin
	-- initialize memory 
		mem(0) := X"20020005";	--			addi 	$2, $0, 5		# $2 = 5
		mem(1) := X"2003000c";	--			addi 	$3, $0, 12		# $3 = 12
		mem(2) := X"2067fff7";	--			addi 	$7, $3, -9		# $7 = $3(12) - 9 = 3
		mem(3) := X"00e22025";	--			or 		$4, $7, $2		# $4 = $7(3) or $2(5) = 7
		mem(4) := X"00642824";	--			and 	$5, $3, $4		# $5 = $3(12) and $4(7) = 4
		mem(5) := X"00a42820";	--			add 	$5, $5, $4		# $5 = $5(4) + $4(7) = 11
		mem(6) := X"10a7000a";	--			beq 	$5, $7, end		# $5(11) == $7(3) ? end : continue
		mem(7) := X"0064202a";	--			slt 	$4, $3, $4		# $3(12) < $4(7)? $4 = 1 : $4 = 0
		mem(8) := X"10800001";	--			beq 	$4, $0, around	# $4(0) == 0 ? around : continue (around)
		mem(9) := X"20050000";	--			addi 	$5, $0, 0
		mem(10) := X"00e2202a";	--around: 	slt 	$4, $7, $2		# $7(3) < $2(5) ? $4 = 1 : $4 = 0
		mem(11) := X"00853820";	--			add 	$7, $4, $5		# $7 = $4(1)+$5(11) = 12
		mem(12) := X"00e23822";	--			sub 	$7, $7, $2		# $7 = $7(12) - $2(5) = 7
		mem(13) := X"ac670044";	--			sw 		$7, 68($3)		# $7(7) -> M[68+$3(12)=80]#test 1
		mem(14) := X"8c020050";	--			lw 		$2, 80($0)		# $2 = M[80+0](7) = 7
		mem(15) := X"08000011";	--			j 		end				# goto end
		mem(16) := X"20020001";	--			addi 	$2, $0, 1
		mem(17) := X"ac02003C";	--end: 		sw 		$2, 60($0) 		# write adr 60=7; #test 2
		mem(18) := X"30A5FFF3"; -- 			andi 	$5,$5,0xFFF3	# $5(11) = $5(11) and 0xFFF3 = 3
		mem(19) := X"00031840"; -- 			sll 	$3, $3, 1		# $3(12) << 1 = $3(24)
		mem(20) := X"ac030046"; -- 			sw 		$3, 70($0)		# $3(24) -> M[70](0) => 24; #test 3
		mem(21) := X"00031882"; -- 			srl 	$3, $3, 2		# $3(24) >> 2 = $3(6)
		mem(22) := X"ac030048"; -- 			sw 		$3, 72($0)		# $3(6) -> M[72](0) => 6; #test 4
		mem(23) := X"0c000019"; -- 			jal 	f				# call f
		mem(24) := X"0800001B"; -- 			j		cont
		mem(25) := X"20040000"; -- f:		addi	$4,$0,0			# $4(1) = 0			
		mem(26) := X"03e00008"; --			jr 		$ra
		mem(27) := X"3c048000";	--cont:		lui 	$4,0x8000		# $4(0) = 0x80000000
		mem(28) := X"3484ffff";	--			ori		$4,$4,0xFFFF	# $4(0x80000000)or 0x0000FFFF = $4(0x8000FFFF)
		mem(29) := X"20057fff";	--			addi 	$5, $0, 0x7FFF		# $5 = 0x7FFF
		mem(30) := X"0005282a";	--			slt	$5,$0,$5	# $0 < $5 ? $5 = 1 : $5 = 0 => $5 = 1
		mem(31) := X"0004102a";	--			slt		$2,$0,$4		# $0 < $4(0x8000FFFF)? $2(7) = 1 : $2(7) = 0 => $2 = 0
		mem(32) := X"ac02004a";	--			sw		$2,74($0)		# $2(0) -> M[74](0) => 0; #test 5
		mem(33) := X"ac040054";	--			sw		$4,84($0)		# $4(0x8000FFFF) -> M[84](0x00000000) => 0x8000FFFF;
		mem(34) := X"ac050056";	--			sw		$5,86($0)		# $5(1) -> M[86](0x00000000) => 1; #test 6			
	-- read memory
		rd <= mem(CONV_INTEGER(a));
end process;
end;
