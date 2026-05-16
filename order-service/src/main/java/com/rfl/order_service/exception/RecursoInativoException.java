package com.rfl.order_service.exception;

public class RecursoInativoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecursoInativoException(String mensagem) {
        super(mensagem);
    }
}