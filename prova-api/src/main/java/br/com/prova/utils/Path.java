package br.com.prova.utils;

public class Path {

	public static final String PATH_CADASTRO = "/api/cliente/**";
	public static final String PATH_AUTENTICAR = "/api/usuario/autenticar";
	
	private Path() {
		throw new IllegalStateException("Constantes utility class");
	}
}
