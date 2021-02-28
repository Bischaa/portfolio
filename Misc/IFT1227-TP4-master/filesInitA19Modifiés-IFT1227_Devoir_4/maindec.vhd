library IEEE; use IEEE.STD_LOGIC_1164.all;

entity maindec is -- main control decoder
	port (op: in STD_LOGIC_VECTOR (5 downto 0);
			regwrite, branch, memwrite: out STD_LOGIC;
			regdst, alusrc, memtoreg: out STD_LOGIC_VECTOR (1 downto 0);
			jump: out STD_LOGIC_VECTOR (1 downto 0);
			byte: out STD_LOGIC;
			aluop: out STD_LOGIC_VECTOR (2 downto 0));
end;

architecture behave of maindec is
	signal controls: STD_LOGIC_VECTOR(14 downto 0);
begin
process(op) begin
	case op is
		when "000000" => controls <= "101000000000111"; -- Rtype
		when "100011" => controls <= "100010001000000"; -- LW
		when "101011" => controls <= "000010100000000"; -- SW
		when "000100" => controls <= "000001000000001"; -- BEQ
		when "001000" => controls <= "100010000000000"; -- ADDI
		when "000010" => controls <= "000000000010000"; -- J
		when "000011" => controls <= "110000010010000"; -- JAL
		when "001101" => controls <= "100100000000011"; -- ORI
		when "001111" => controls <= "100000011000000"; -- LUI
		when "001100" => controls <= "100100011000010"; -- ANDI
		when "100000" => controls <= "100010001001000"; -- LB
		when others => controls <= "---------"; -- illegal op
	end case;
end process;

	regwrite <= controls(14);
	regdst <= controls(13 downto 12);
	alusrc <= controls(11 downto 10);
	branch <= controls(9);
	memwrite <= controls(8);
	memtoreg <= controls(7 downto 6);
	jump <= controls(5 downto 4);
	byte <= controls(3)
	aluop <= controls(2 downto 0);
end;