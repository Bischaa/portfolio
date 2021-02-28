library IEEE; use IEEE.STD_LOGIC_1164.ALL;
use IEEE.numeric_std.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity alu is
	port (	a, b: in STD_LOGIC_VECTOR(31 downto 0);
			f: in STD_LOGIC_VECTOR (5 downto 0);
			shamt: in STD_LOGIC_VECTOR (4 downto 0);
			z, o : out STD_LOGIC;
			y: out STD_LOGIC_VECTOR(31 downto 0):= X"00000000");
end;
architecture behave of alu is
signal diff, tmp: STD_LOGIC_VECTOR(31 downto 0);
begin
	diff <= a - b;
	process (a, b, f, diff, shamt) begin
		case f is
			when "100000" => tmp <= a + b; --32 add
			when "100010" => tmp <= a - b; --34 sub
			when "100100" => tmp <= a and b; --36 and
			when "100101" => tmp <= a or b; --37 or
			when "100110" => tmp <= a xor b; --38 xor
			when "100111" => tmp <= not (a or b); --39 nor 
			when "101010" => --SLT
					if diff(31) = '1' then 
						tmp <= X"00000001"; 
					else 
						tmp <= X"00000000"; 
					end if;
			when "000000" => tmp <= shl(b,shamt) ; -- 0 sll
			when "000010" => tmp <= shr(b,shamt) ; -- srl
			when others => tmp <= X"00000000"; 
		end case;
	end process;
	
	z <= '1' when tmp = X"00000000" else '0';
	o <= '1' when a(31) = b(31) and (a(31) /= tmp(31)) else '0';
	y <= tmp;
	
end;