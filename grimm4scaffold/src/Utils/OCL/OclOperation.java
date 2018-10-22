package Utils.OCL;

/**
 * Enumère les opérations disponibles en OCL et en XCSP afin de faciliter leur transformation et leur traitement
 */
public enum OclOperation {
	/* BOOLEAN */
	eq(OclOperationType.Boolean, "=", 2),
	ne(OclOperationType.Boolean, "<>", 2),
	ge(OclOperationType.Boolean, ">=", 2),
	gt(OclOperationType.Boolean, ">", 2),
	le(OclOperationType.Boolean, "<=", 2),
	lt(OclOperationType.Boolean, "<", 2),

	not(OclOperationType.Boolean, "not", 1),
	and(OclOperationType.Boolean, "and", 2),
	or(OclOperationType.Boolean, "or", 2),
	xor(OclOperationType.Boolean, "xor", 2),
	//iff(OclOperationType.Boolean, "todo", 2),

	/* NUMBER */
	neg(OclOperationType.Number, "-", 1),
	add(OclOperationType.Number, "+", 2),
	sub(OclOperationType.Number, "-", 2),
	mul(OclOperationType.Number, "*", 2),
	div(OclOperationType.Number, "/", 2),
	
	/* COLLECTION */
	forAll(OclOperationType.Collection, "forAll", 1),
	
	/* OTHER */
	oclType(OclOperationType.Other, "oclType", 1)
	//ifCond(OclOperationType.Number, "if", 3)
	;

	private OclOperationType _type;
	private int _arity;
	private String _oclSymbol;

	/**
	 * Créé une opération OCL et XCSP
	 * @param type Type de l'opération {@link OclOperationType}
	 * @param oclSymbol Symbole de l'opération. Par exemple, pour l'addition le symbole sera "+"
	 * @param arity Arité de l'opération. Correspond au nombre de paramètres
	 */
	private OclOperation(OclOperationType type, String oclSymbol, int arity) {
		_type = type;
		_arity = arity;
		_oclSymbol = oclSymbol;
	}

	/**
	 * Récupère le type de l'opération. 
	 * @return Le type de l'opération
	 * @see OclOperationType
	 */
	public OclOperationType getType() {
		return _type;
	}
	
	/**
	 * Récupère l'arité de l'opération qui correspond à son nombre de paramètres requis.
	 * @return l'arité de l'opération
	 */
	public int getArity() {
		return _arity;
	}
	
	/**
	 * Récupère le symbole correspondant à l'opération. Par exemple, pour l'addition le symbole sera "+".
	 * @return Le symbole correspondant à l'opération
	 */
	public String getOclSymbol() {
		return _oclSymbol;
	}

	/**
	 * Fonction qui récupère une opération OCL à partir d'un symbole et d'une arité particulière.
	 * @param oclSymbol Symbole de l'opération
	 * @param arity Arité de l'opération
	 * @return L'opération OCL correspondante
	 */
	public static OclOperation getOperationFromOCLSymbol(String oclSymbol, int arity) {
		for(OclOperation op : OclOperation.values()) {
			if (op.getOclSymbol().equals(oclSymbol) && op.getArity() == arity)
				return op;
		}
		System.err.println("Operation '" + oclSymbol + "' non prise en charge");
		return null;
	}
}
