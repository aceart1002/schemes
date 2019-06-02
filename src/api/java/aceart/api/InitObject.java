package aceart.api;

public final class InitObject {
	
	public static Printable printer;
	public static Saving saver;
	public static Controlling controller;
	public static ServerUpdater updater;
	
	public InitObject(Printable printer, Saving saver, Controlling controller, ServerUpdater updater) {
		this.printer = printer;
		this.saver = saver;
		this.controller = controller;
		this.updater = updater;
	}
//	
//	public static void setController(Controlling newCont) {
//		controller = newCont;
//		CommonProxy.controller = controller;
//	}
	
}
