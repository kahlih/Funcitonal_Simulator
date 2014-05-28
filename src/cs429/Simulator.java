package cs429;

/* instruction encoding
 * 
 * 0 00000 00    halt
 * 
 * 0 00011 dd    jle dd
 * 0 00110 dd    outch dd
 * 0 00111 dd    show dd
 * 0 01000 dd    ldi dd
 * 
 * 
 * 1 000 ss dd   add ss,dd
 * 1 011 ss dd   cmp ss,dd
 * 
 */

public class Simulator {

	public static boolean DEBUG = false;

	public static void debug(String msg, Object... args) {
		if (DEBUG) {
			System.out.printf(msg, args);
		}
	}

	public static void outch(byte val) {
		System.out.printf("%c", val);
	}

	public static void show(int reg, byte val) {
		System.out.printf("reg[%d] = %d 0x%x\n", reg, val, val);
	}

	public static byte run(byte mem[]) {
		int Registers[] = new int[5];
		byte pc = 0;
		for (int i = 0; i < mem.length; i++)  //PC
		{
			byte mine = mem[i];
			int num; int dd;
			if (mine < 0)
			{
				num = mine + 256;
				dd = num & 0x3;
				int ss = (num>>2) & 0x3;
				
				//add
				if (((num>>4) & 0xF) == 0x8)                   
				{
					//System.out.println("This is an ADD Function");
					Registers[dd] = Registers[ss] + Registers[dd];
				}	 
		
				//cmp
				else if (((num>>4) & 0xF) == 0xB)           
				{	
					Registers[4] = Registers[dd]-Registers[ss];
				}
				/*else ---- terminate*/
				else
					break; 
			}
			else
			{
				num = mine;
				dd = num & 0x3;

				// movi
				if (((num>>2) & 0x3F) == 0x8)
				{
					Registers[dd] = mem[i+1];
					i = i+1;
					pc++;
				}
				// show
				else if (((num>>2) & 0x3F) == 0x7)
				{
					show(dd, (byte)Registers[dd]);	
				}
				// outch
				else if (((num>>2) & 0x3F) == 0x6)
				{
					outch((byte)Registers[dd]);	
				}
				// jle
				else if (((num>>2) & 0x3F) == 0x3)
				{
					if (Registers[4] <= 0)
					{
						i=(Registers[dd]-1);
						pc = (byte)i;
					}
				}
				//halt
				else //if(((num>>2) & 0x3F) == 0x0)
				{
					break;
				}
		//		else
		//			continue;
			}
			pc++;	//increment pc return value
		}
	return pc; 
	}

}
