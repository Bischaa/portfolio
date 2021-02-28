library IEEE; use IEEE.STD_LOGIC_1164.all;
entity controller is -- single cycle control decoder
	port (op, funct: in STD_LOGIC_VECTOR (5 downto 0);
			zero: in STD_LOGIC;
			memwrite: out STD_LOGIC;
			pcsrc: out STD_LOGIC;
			regwrite: out STD_LOGIC;
			regdst, memtoreg, alusrc: out STD_LOGIC_VECTOR(1 downto 0);
			jump: out STD_LOGIC_VECTOR (1 downto 0);
			byte, branch: out STD_LOGIC
			alucontrol: out STD_LOGIC_VECTOR (5 downto 0));
end;

architecture struct of controller is
	component maindec
		port (op: in STD_LOGIC_VECTOR (5 downto 0);
			regwrite, branch, memwrite: out STD_LOGIC;
			regdst, alusrc, memtoreg: out STD_LOGIC_VECTOR (1 downto 0);
			jump: out STD_LOGIC_VECTOR (1 downto 0);
			byte: out STD_LOGIC;
			aluop: out STD_LOGIC_VECTOR (2 downto 0));
	end component;
	component aludec
		port (funct: in STD_LOGIC_VECTOR (5 downto 0);
				aluop: in STD_LOGIC_VECTOR (1 downto 0);
				alucontrol: out STD_LOGIC_VECTOR (5 downto 0));
	end component;
	signal aluop: STD_LOGIC_VECTOR (2 downto 0);
	signal branch: STD_LOGIC;
begin
	md: maindec port map (op, memtoreg, memwrite, branch, alusrc, regdst, regwrite, jump, byte, aluop);
	ad: aludec port map (funct, aluop, alucontrol);
	pcsrc <= branch and zero;
end;