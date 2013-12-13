package bitcraftlab.logo;

import processing.core.PApplet;
import org.nlogo.headless.HeadlessWorkspace;
import java.io.File;
import java.io.IOException;
import org.nlogo.api.CompilerException;
import org.nlogo.api.LogoException;

import org.nlogo.api.AgentSet;
import org.nlogo.api.Agent;
import org.nlogo.api.LogoList;
import java.util.ArrayList;

public class NetLogo {

  HeadlessWorkspace workspace = HeadlessWorkspace.newInstance() ;
  PApplet app;
  String model;


  public NetLogo(PApplet app) {

  	this.app = app;
  	app.registerDispose(this);

  }

  public NetLogo(PApplet app, String model) {

  	this(app);
  	open(model);

  }

  /** Open a NetLgoo Model */
  public void open(String model) {

  	// try to locate the file in the data dir
  	String path = app.dataPath(model);
	File file = new File(path);

	// if the file is not there look in the sketch dir
	if(!file.exists()) {

		path = app.sketchPath(model);
		file = new File(path);
		
	}
	
	if(file.exists()) {

		// try to open the model in the netlogo workspace
		try {
			
			workspace.open( path );

			// Some feedback
  			System.out.println("Opened workspace: " + model);
  			this.model = model;


		} catch(IOException ioe) {
			
			// we found the model but could not open it
			System.out.println("Error opening model: " + model);

		} catch(CompilerException ce) {

			// Compiler error ...
			System.out.println("Compiler Error in Netlogo model: " + model);
			ce.printStackTrace();
		
		} catch(LogoException le) {
		
			// Runtime Error
			System.out.println("Runtime Error in NetLogo model: " + model);
			le.printStackTrace();

  		}


	} else {

		// give up and provide some feedback
		System.out.println("Could not find model: " + model);
		System.out.println("Make sure it's inside your sketch or data folder");

	}


  }


  /**  Send a command to NetLogo */
  public void cmd(String cmd) {

  	try {

  		// pass the command to the netlogo compiler
  		workspace.command(cmd);

  	} catch(CompilerException ce) {

  		System.out.println("Compiler Error in NetLogo command: " + cmd);
  		ce.printStackTrace();

  	} catch(LogoException le) {
		
		System.out.println("Runtime Error in NetLogo command: " + cmd);
		le.printStackTrace();

  	}

  }


  /* Ask NetLogo to evaluate or report something */
  public Object report(String cmd) {

  	Object result = null;

  	try {

		// The resulting object may be an Integer, Double, Boolean,
		// String, LogoList, Agent, AgentSet or Nobody.
		// See: HeadlessWorkspace.scala 
  		result = workspace.report(cmd);

  	} catch(CompilerException ce) {

  		System.out.println("Compiler Error in NetLogo command: " + cmd);
  		ce.printStackTrace();

  	} catch(LogoException le) {
		
		System.out.println("Runtime Error in NetLogo command: " + cmd);
		le.printStackTrace();

  	}

  	return result;

  }

  // should be run when the sketch closes
  public void dispose() {

    if(workspace != null) {
	 
	  	try {
	  		workspace.dispose();
	  		System.out.println("Closed workspace: " + model);

	  	} catch(Exception ex) {

			System.out.println("Error closing workspace: " + model);

	  	}

	}

  }


  public String eval(String cmd) {
  	return report(cmd).toString();
  }

  public AgentSet agentset(String cmd) {
  	return (AgentSet) report(cmd);
  }

  // make an arraylist of agents, for processing users
  public ArrayList<Agent> agents(String cmd) {
    Iterable<Agent> agents = agentset(cmd).agents();
    ArrayList<Agent> list = new ArrayList<Agent>();
    for(Agent a : agents) list.add(a);
  	return list;
  }

  public Agent agent(String cmd) {
  	return (Agent) report(cmd);
  }

  public LogoList logolist(String cmd) {
  	return (LogoList) report(cmd);
  }

  public boolean booleanValue(String cmd) {
  	return (Boolean) report(cmd);
  }

  public float number(String cmd) {
  	return ((Double) report(cmd)).floatValue();
  }

}
