package input.placementtest.src;


public class placementcheckertestinput {
	
	
	String dataLeAk2 = "2";

	String dataLeAk1 = "1";

	String dataLeAk0 = "0";

	public placementcheckertestinput() {
		dataLeAk0 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
	
	}
	
	public void printA() {
		dataLeAk1 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		System.out.println();
	}
	
	public void printB() {
		dataLeAk2 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		System.out.println();
	
	}


}
