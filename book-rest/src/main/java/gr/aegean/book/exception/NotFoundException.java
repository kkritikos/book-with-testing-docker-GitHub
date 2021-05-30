package gr.aegean.book.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebFault;

import gr.aegean.book.domain.ErrorItem;

/**
 * Thrown when a resource requested is not found.
 *
 * @author Kyriakos Kritikos
 */
@WebFault
public class NotFoundException extends WebApplicationException {
	public NotFoundException(String message){
		super(Response.status(Response.Status.NOT_FOUND).entity(new ErrorItem(Response.Status.NOT_FOUND.getStatusCode(), "not_found", message)).type(MediaType.APPLICATION_XML).build());
	}
	
	public NotFoundException(){
		super();
	}
}
