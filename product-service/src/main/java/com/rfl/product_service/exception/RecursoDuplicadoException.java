
package com.rfl.product_service.exception;

public class RecursoDuplicadoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecursoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}