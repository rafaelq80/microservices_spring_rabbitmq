
package com.rfl.notification_service.exception;

public class RecursoDuplicadoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecursoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}