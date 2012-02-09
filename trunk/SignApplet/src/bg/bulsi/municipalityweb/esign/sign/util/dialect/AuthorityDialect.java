 package bg.bulsi.municipalityweb.esign.sign.util.dialect;
 
 import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import bg.bulsi.municipalityweb.esign.sign.util.CertificateUtil;
 
 public class AuthorityDialect
 {
   private String id;
   private String separator;
   private String name;
   private String email;
   private String egn;
   private String bulstat;
 
   public Map<AuthorityDialectProperty, String> extractData(X509Certificate cert)
   {
     HashMap<AuthorityDialectProperty, String> resultMap = new HashMap<AuthorityDialectProperty, String>();
     try
     {
       String authorityKey = CertificateUtil.getAuthorityKey(cert);
 
       Map<String, String> subjectMap = fillCertSubjectMap(cert.getSubjectX500Principal().getName("RFC1779"), getSeparator());
 
       String sn = cert.getSerialNumber().toString();
       String email = (String)subjectMap.get(getEmail());
       String egn = (String)subjectMap.get(getEgn());
 
       if (egn != null) {
         egn = egn.replaceAll("[^0-9]", "");
       }
       String name = (String)subjectMap.get(getName());
       String bulstat = (String)subjectMap.get(getBulstat());
 
       resultMap.put(AuthorityDialectProperty.KEY, authorityKey);
       resultMap.put(AuthorityDialectProperty.CERT_SERIAL_NUM, sn);
       resultMap.put(AuthorityDialectProperty.NAME, name);
       resultMap.put(AuthorityDialectProperty.EMAIL, email);
       resultMap.put(AuthorityDialectProperty.BULSTAT, bulstat);
       resultMap.put(AuthorityDialectProperty.EGN, egn);
     }
     catch (Exception e) {
       e.printStackTrace();
     }
     return resultMap;
   }
 
   private Map<String, String> fillCertSubjectMap(String rfc1779, String separator)
   {
     Map<String, String> subjectMap = new HashMap<String, String>();
     String regex = new String("({0})+(?=[\\w\\.]+=)");
     Object[] args = { separator };
 
     String fullSeparator = MessageFormat.format(regex, args);
     String[] pairs = rfc1779.split(fullSeparator);
 
     for (int i = 0; i < pairs.length; i++) {
       String[] values = pairs[i].split("=");
       subjectMap.put(values[0], values[1]);
     }
     return subjectMap;
   }
 
   public String getId()
   {
     return this.id;
   }
 
   public void setId(String id)
   {
     this.id = id;
   }
 
   public String getSeparator()
   {
     return this.separator;
   }
 
   public void setSeparator(String separator)
   {
     this.separator = separator;
   }
 
   public String getName()
   {
     return this.name;
   }
 
   public void setName(String name)
   {
     this.name = name;
   }
 
   public String getEmail()
   {
     return this.email;
   }
 
   public void setEmail(String email)
   {
     this.email = email;
   }
 
   public String getEgn()
   {
     return this.egn;
   }
 
   public void setEgn(String egn)
   {
     this.egn = egn;
   }
 
   public String getBulstat()
   {
     return this.bulstat;
   }
 
   public void setBulstat(String bulstat)
   {
     this.bulstat = bulstat;
   }
 }




