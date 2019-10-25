package utils.electricaldata.ifsuldeminas.edu.br;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import beans.electricaldata.ifsuldeminas.edu.br.ElectricalData;
import services.electricaldata.ifsuldeminas.edu.br.ElectricalDataService;

@Path("/file")
public class FileResource extends HttpServlet {
	
	@Context
    private HttpServletRequest request;	

	private static final long serialVersionUID = 1L;
	private static String _filesPath = "context.path";
	private static String _fileColumnSeparator = "fileColumnSeparator";
	private static String _showNumberConvertionMessages = "showNumberConvertionMessages";
	
	private int placeId;
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(
			@FormDataParam("entity") FormDataBodyPart jsonPart,
			@FormDataParam("uploadfile") InputStream uploadedInputStream,
			@FormDataParam("uploadfile") FormDataContentDisposition fileDetail) {

		setPlaceId(jsonPart);
		String fullPath = saveFile(placeId, uploadedInputStream, fileDetail);
		return processFile(fullPath);			
	}
	
	private void setPlaceId(FormDataBodyPart jsonPart) {
		jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
	    ElectricalData electricalData = jsonPart.getValueAs(ElectricalData.class);
	    placeId = electricalData.getPlace().getPlaceId();
	}	

	private String saveFile(int placeId, InputStream uploadedInputStream, FormDataContentDisposition fileDetail) {
		String fileName = fileDetail.getFileName();
		String path = createDirectoryStructure(placeId);
		String fullPath = path + fileName;
		int fileSize;
		try {
			System.out.println("Path to save file: " + fullPath);
			OutputStream outStream = new FileOutputStream(fullPath);
			fileSize = IOUtils.copy(uploadedInputStream, outStream);
			fileSize = fileSize / 1024;
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
		return fullPath;
	}
	
	private String createDirectoryStructure(int parameter) {
		String path = getFilesPath() + File.separatorChar + String.valueOf(parameter) + File.separatorChar;
		File targetDir = new File(path);

		if (!targetDir.exists()) {
			System.out.println("Criando Estrutura de Pastas: " + targetDir.getPath());
			boolean result = false;

			try {
				targetDir.mkdirs();
				result = true;
			} catch (SecurityException se) {
				throw se;
			}
			if (result) {
				System.out.println("Estrutura de Pastas Criada!");
			}
		}
		return path;
	}
	
	private String getFilesPath() {
		return PropsUtil.getInstance().getProperty(_filesPath);
	}
	
	private Response processFile(String fullPath) {
		
		try {
			List<String> fileLines = getFileLines(fullPath);
			List<String[]> splittedFieldLines = processLinesIntoArray(fileLines);
			insertLines(splittedFieldLines);
			return Response.ok("Arquivo Importado com Sucesso!", MediaType.TEXT_PLAIN).build();
		} catch (final Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
	}
	
	public static List<String> getFileLines(String fullPath) {
		try {			
		    
			return Files.readAllLines(Paths.get(fullPath), StandardCharsets.ISO_8859_1);			
		} catch(IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(e);	    		
    	} 
	}
	
	private List<String[]> processLinesIntoArray(List<String> fileLines) throws Exception {
	    List<String[]> splittedLines = new ArrayList<>();
        for (int i = 1; i < fileLines.size(); i++)
        {
            if (!fileLines.get(i).isEmpty())
            {
                String[] stringValues = processLine(fileLines.get(i), getFileColumnSeparator());
                splittedLines.add(stringValues);
            }
        }
        return splittedLines;          
	}
	
	public static String[] processLine(String line) throws Exception {
		String fileColumnSeparator = getFileColumnSeparator();
		return processLine(line, fileColumnSeparator);
	}
	
	public static String[] processLine(String line, String fileColumnSeparator) throws Exception {
		if (line == null || line.equals(""))
            throw new Exception("Valor de linha é nulo ou vazio.");
				
		line = convertDecimalSeparator(line);
		
        String[] splittedLine = line.split(fileColumnSeparator);
		
		if (splittedLine.length == 1)
            throw new Exception("As colunas estão em um formato incorreto.");
		
		String[] convertedLineValues = convertToFloat(splittedLine);
		convertedLineValues = formatDateValue(convertedLineValues);
		return convertedLineValues;	
	}
	
	private static String getFileColumnSeparator() {
		return PropsUtil.getInstance().getProperty(_fileColumnSeparator);
	}	
	
    private static String convertDecimalSeparator(String lineValue)
    {        
        char decimalSeparator = getDecimalSeparator();
        if (showConvertionMessages()) {
			System.out.println("Decimal Separator: " + decimalSeparator);
		}
		switch (decimalSeparator) {
			case ',':
				lineValue = lineValue.replace('.',decimalSeparator);
				break;
			case ';':
				lineValue = lineValue.replace(',',decimalSeparator);
				break;
        }       
        return lineValue;
    }
	
    private static char getDecimalSeparator() {
    	DecimalFormatSymbols symbols = new DecimalFormatSymbols() ;
    	return symbols.getDecimalSeparator();
    }
    
    private static boolean showConvertionMessages() {
    	return Boolean.parseBoolean(PropsUtil.getInstance().getProperty(_showNumberConvertionMessages));
    }
    
    private static String[] convertToFloat(String[] values) {
    	String[] convertedValues = new String[values.length];
    	convertedValues[0] = values[0];
    	for (int dataCount = 1; dataCount < values.length; dataCount++) {            
            Scanner n = new Scanner(values[dataCount]);            
            convertedValues[dataCount] = "0";
            if(n.hasNextFloat()){
            	convertedValues[dataCount] = String.valueOf(n.nextFloat());
            } 
            n.close();
            if (showConvertionMessages()) {
            	System.out.println(String.format("Original / Convertido: %s -- %s", values[dataCount], convertedValues[dataCount]));	
            }            
        }
    	return convertedValues;
    }
	
	public static String[] formatDateValue(String[] lineValues) throws Exception {
		try {
			lineValues[0] = "'" + DateUtil.createDateFormatMySql(lineValues[0]) + "'";
		} catch (ParseException e) {			
			e.printStackTrace();
		}
        return lineValues;    
	}
	
	private void insertLines(List<String[]> splittedLines) {
		ElectricalDataService service = new ElectricalDataService(request);
        service.insertRowsInDatabase(placeId, splittedLines);	  
	}
}
