 package bg.bulsi.municipalityweb.esign.sign.util;
 
 public class DigitalSignatureException extends Exception
 {
	private static final long serialVersionUID = 1L;

public DigitalSignatureException(String aMessage)
   {
     super(aMessage);
   }
 
   public DigitalSignatureException(String aMessage, Throwable aCause)
   {
     super(aMessage, aCause);
   }
 }




