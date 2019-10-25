package utils.electricaldata.ifsuldeminas.edu.br;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;

/**
 * The Class PropsUtil.
 */
public class PropsUtil {
  
    /** The _props. */
    private static Properties _props = new Properties();
    
    /** The _hash props. */
    private static HashMap<String, Properties> _hashProps = new HashMap<>();
    
    /**
     * Instantiates a new props util using a path.
     *
     * @param path the path
     */
    private PropsUtil(String path) {
    }
    
    /**
     * Instantiates a new props util.
     */
    private PropsUtil() {
    }
    
    /**
     * Gets the single instance of PropsUtil.
     *
     * @return single instance of PropsUtil
     */
    public static synchronized Properties getInstance() {
    	String pathDef = "/electricaldata.properties";
    	_props = getInstance(pathDef);
    	return _props;
    }    

    
    /**
     * Gets the single instance of PropsUtil.
     *
     * @param path the path
     * @return single instance of PropsUtil
     */
    public static synchronized Properties getInstance(String path) {
    	_props = _hashProps.get(path);
    	if(_props==null){
    		_props = new Properties();
    		loadFile(path);
    		_hashProps.put(path, _props);
    	}
    	return _props;
    }    

    /**
     * Gets the.
     *
     * @param key the key
     * @return the string
     */
    public String getKey(String key){
        Object obj = _props.get(key);
        return (obj==null?null:(String)obj);
    }    
   
    /**
     * Load file using a Path.
     *
     * @param path the path
     */
    private static void loadFile(String path){
        loadCommonFile(path);
    }

	/**
	 * Load common file.
	 *
	 * @param path the path
	 */
    private static void loadCommonFile(String path) {
    	String originalPath = new String(path); 
    	InputStream is = getExternalPath(path);
    	if(is == null){
    		is = PropsUtil.class.getResourceAsStream(originalPath);	
    	}
    	try {
    		_props.load(is);
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
    }

	/**
	 * Gets the external path.
	 *
	 * @param path the path
	 * @return the external path
	 */
    public static InputStream getExternalPath(String path) {
    	InputStream is = null;
    	File f;
    	String pathJar = getPathJar();

    	f = new File(pathJar+path);
    	ProjectLogger.log.info(f.getPath()+" - exist = "+f.exists());
    	if(f.exists()){
    		try {
    			is = new FileInputStream(f);
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
    	}
    	return is;
    }

	/**
	 * Gets the external path file.
	 *
	 * @param path the path
	 * @return the external path file
	 */
    public static File getExternalPathFile(String path) {
    	File f = null;
    	String pathJar = getPathJar();
    	f = new File(pathJar+path);
    	ProjectLogger.log.info(f.getPath());
    	return f;
    }

	/**
	 * Gets the path jar.
	 *
	 * @param c the c
	 * @return the path jar
	 */
	public static String getPathJar(Class<?> c) {
		String pathJar = c.getProtectionDomain().getCodeSource().getLocation().getPath();
		ProjectLogger.log.info(" pathJar "+pathJar);
		File f = new File(pathJar);
		pathJar = f.getPath();
		ProjectLogger.log.info(" pathJar "+pathJar);
		if(pathJar.endsWith(".jar") || pathJar.endsWith(".exe")){
			int delimiter = pathJar.lastIndexOf("\\");
			if(delimiter != -1){
				pathJar = pathJar.substring(0,delimiter);	
			}
		}
		try {
			pathJar=URLDecoder.decode(pathJar, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ProjectLogger.log.info(" pathJar "+pathJar);
		return pathJar;
	}
	
	/**
	 * Gets the path jar.
	 *
	 * @return the path jar
	 */
	public static String getPathJar() {
		String pathJar = getPathJar(PropsUtil.class);
		return pathJar;
	}
}
