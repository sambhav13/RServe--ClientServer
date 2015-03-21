package goliath.analytics.mllib;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class RServeMultiThread extends Thread {

	   private int port = 0;

	   public RServeMultiThread(int port) {
	       this.port = port;
	   }

	   public void run() {
		   
		   RConnection c = null;
	       try {
	           c = new RConnection("172.22.95.107", 1000);
	           c.eval("N = " + port);
	           c.eval("x1=rnorm(N)");
	           c.eval("x2 = 1 + x1 + rnorm(N)");
	           c.eval("y <- 1 + x1 + x2");
	           c.eval("df <- data.frame(y,x1,x2)");
	           c.eval("fit <- lm(y ~ x1 + x2, data = df)");
	           REXP x1 = c.eval("fit[[1]][2]");
	           System.out.println("Thread with port " + port + " result: "
	                   + x1.asInteger());
	          // Thread.sleep(3000);
	       } catch (RserveException e1) {
	           e1.printStackTrace();
	       } /*catch (InterruptedException e) {
	           e.printStackTrace();
	       }*/ catch (REXPMismatchException e) {
	           e.printStackTrace();
	       }
	       finally{
	    	   c.close();
	       }
	   }
	}
