package MiniJava.codeGenerator;

import MiniJava.Log.Log;
import MiniJava.errorHandler.ErrorHandler;
import MiniJava.scanner.token.Token;
import MiniJava.semantic.symbol.Symbol;
import MiniJava.semantic.symbol.SymbolTable;
import MiniJava.semantic.symbol.SymbolType;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Alireza on 6/27/2015.
 */
public class CodeGenerator {
	private Memory memory = new Memory();
	private Stack<Address> ss = new Stack<Address>();
	private Stack<String> symbolStack = new Stack<>();
	private Stack<String> callStack = new Stack<>();
	private SymbolTable symbolTable;

	public Memory getMemory() {
		return memory;
	}

	public Stack<Address> getSs() {
		return ss;
	}

	public Stack<String> getSymbolStack() {
		return symbolStack;
	}

	public Stack<String> getCallStack() {
		return callStack;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public HashMap<Integer, Action> numberToAction;

	public CodeGenerator() {
		symbolTable = new SymbolTable(getMemory());
		// TODO
		numberToAction = new HashMap<>();
		numberToAction.put(1, new CheckIdAction());
		numberToAction.put(2, new PidAction());
		numberToAction.put(3, new FPidAction());
		numberToAction.put(4, new KPidAction());
		numberToAction.put(5, new IntPidAction());
		numberToAction.put(6, new StartCallAction());
		numberToAction.put(7, new CallAction());
		numberToAction.put(8, new ArgAction());
		numberToAction.put(9, new AssignAction());
		numberToAction.put(10, new AddAction());
		numberToAction.put(11, new SubAction());
		numberToAction.put(12, new MultAction());
		numberToAction.put(13, new LabelAction());
		numberToAction.put(14, new SaveAction());
		numberToAction.put(15, new WhileAction());
		numberToAction.put(16, new JpfSaveAction());
		numberToAction.put(17, new JpHereAction());
		numberToAction.put(18, new PrintAction());
		numberToAction.put(19, new EqualAction());
		numberToAction.put(20, new LessThanAction());
		numberToAction.put(21, new AndAction());
		numberToAction.put(22, new NotAction());
		numberToAction.put(23, new DefClassAction());
		numberToAction.put(24, new DefMethodAction());
		numberToAction.put(25, new PopClassAction());
		numberToAction.put(26, new ExtendAction());
		numberToAction.put(27, new DefFieldAction());
		numberToAction.put(28, new DefVarAction());
		numberToAction.put(29, new MethodReturnAction());
		numberToAction.put(30, new DefParamAction());
		numberToAction.put(31, new LastTypeBoolAction());
		numberToAction.put(32, new LastTypeIntAction());
		numberToAction.put(33, new DefMainAction());
	}

	public void printMemory() {
		getMemory().pintCodeBlock();
	}

	public void semanticFunction(int func, Token next) {
		Log.print("codegenerator : " + func);
		Action action = numberToAction.get(func);
		if (action != null) {
			action.executeAction(this, next);
		}
		// switch (func) {
		// case 0:
		// return;
		// case 1:
		// checkID();
		// break;
		// case 2:
		// pid(next);
		// break;
		// case 3:
		// fpid();
		// break;
		// case 4:
		// kpid(next);
		// break;
		// case 5:
		// intpid(next);
		// break;
		// case 6:
		// startCall();
		// break;
		// case 7:
		// call();
		// break;
		// case 8:
		// arg();
		// break;
		// case 9:
		// assign();
		// break;
		// case 10:
		// add();
		// break;
		// case 11:
		// sub();
		// break;
		// case 12:
		// mult();
		// break;
		// case 13:
		// label();
		// break;
		// case 14:
		// save();
		// break;
		// case 15:
		// _while();
		// break;
		// case 16:
		// jpf_save();
		// break;
		// case 17:
		// jpHere();
		// break;
		// case 18:
		// print();
		// break;
		// case 19:
		// equal();
		// break;
		// case 20:
		// less_than();
		// break;
		// case 21:
		// and();
		// break;
		// case 22:
		// not();
		// break;
		// case 23:
		// defClass();
		// break;
		// case 24:
		// defMethod();
		// break;
		// case 25:
		// popClass();
		// break;
		// case 26:
		// extend();
		// break;
		// case 27:
		// defField();
		// break;
		// case 28:
		// defVar();
		// break;
		// case 29:
		// methodReturn();
		// break;
		// case 30:
		// defParam();
		// break;
		// case 31:
		// lastTypeBool();
		// break;
		// case 32:
		// lastTypeInt();
		// break;
		// case 33:
		// defMain();
		// break;
		// }
	}

	public void defMain() {
		// getSs().pop();
		getMemory().add3AddressCode(getSs().pop().num, Operation.JP,
				new Address(getMemory().getCurrentCodeBlockAddress(), varType.Address), null, null);
		String methodName = "main";
		String className = getSymbolStack().pop();

		getSymbolTable().addMethod(className, methodName, getMemory().getCurrentCodeBlockAddress());

		getSymbolStack().push(className);
		getSymbolStack().push(methodName);
	}

	// public void spid(Token next){
	// getSymbolStack().push(next.value);
	// }
	public void checkID() {
		getSymbolStack().pop();
		if (getSs().peek().varType == varType.Non) {
			// TODO : error
		}
	}

	public void pid(Token next) {
		if (getSymbolStack().size() > 1) {
			String methodName = getSymbolStack().pop();
			String className = getSymbolStack().pop();
			try {

				Symbol s = getSymbolTable().get(className, methodName, next.value);
				varType t = varType.Int;
				switch (s.type) {
					case Bool :
						t = varType.Bool;
						break;
					case Int :
						t = varType.Int;
						break;
				}
				getSs().push(new Address(s.address, t));

			} catch (Exception e) {
				getSs().push(new Address(0, varType.Non));
			}
			getSymbolStack().push(className);
			getSymbolStack().push(methodName);
		} else {
			getSs().push(new Address(0, varType.Non));
		}
		getSymbolStack().push(next.value);
	}

	public void fpid() {
		getSs().pop();
		getSs().pop();

		Symbol s = getSymbolTable().get(getSymbolStack().pop(), getSymbolStack().pop());
		varType t = varType.Int;
		switch (s.type) {
			case Bool :
				t = varType.Bool;
				break;
			case Int :
				t = varType.Int;
				break;
		}
		getSs().push(new Address(s.address, t));

	}

	public void kpid(Token next) {
		getSs().push(getSymbolTable().get(next.value));
	}

	public void intpid(Token next) {
		getSs().push(new Address(Integer.parseInt(next.value), varType.Int, TypeAddress.Imidiate));
	}

	public void startCall() {
		// TODO: method ok
		getSs().pop();
		getSs().pop();
		String methodName = getSymbolStack().pop();
		String className = getSymbolStack().pop();
		getSymbolTable().startCall(className, methodName);
		getCallStack().push(className);
		getCallStack().push(methodName);

		// getSymbolStack().push(methodName);
	}

	public void call() {
		// TODO: method ok
		String methodName = getCallStack().pop();
		String className = getCallStack().pop();
		try {
			getSymbolTable().getNextParam(className, methodName);
			ErrorHandler.printError("The few argument pass for method");
		} catch (IndexOutOfBoundsException e) {
		}
		varType t = varType.Int;
		switch (getSymbolTable().getMethodReturnType(className, methodName)) {
			case Int :
				t = varType.Int;
				break;
			case Bool :
				t = varType.Bool;
				break;
		}
		Address temp = Address.createTempAddress(getMemory(), t);
		getSs().push(temp);
		getMemory().add3AddressCode(Operation.ASSIGN, new Address(temp.num, varType.Address, TypeAddress.Imidiate),
				new Address(getSymbolTable().getMethodReturnAddress(className, methodName), varType.Address), null);
		getMemory().add3AddressCode(Operation.ASSIGN,
				new Address(getMemory().getCurrentCodeBlockAddress() + 2, varType.Address, TypeAddress.Imidiate),
				new Address(getSymbolTable().getMethodCallerAddress(className, methodName), varType.Address), null);
		getMemory().add3AddressCode(Operation.JP,
				new Address(getSymbolTable().getMethodAddress(className, methodName), varType.Address), null, null);

		// getSymbolStack().pop();
	}

	public void arg() {
		// TODO: method ok

		String methodName = getCallStack().pop();
		// String className = getSymbolStack().pop();
		try {
			Symbol s = getSymbolTable().getNextParam(getCallStack().peek(), methodName);
			varType t = varType.Int;
			switch (s.type) {
				case Bool :
					t = varType.Bool;
					break;
				case Int :
					t = varType.Int;
					break;
			}
			Address param = getSs().pop();
			if (param.varType != t) {
				ErrorHandler.printError("The argument type isn't match");
			}
			getMemory().add3AddressCode(Operation.ASSIGN, param, new Address(s.address, t), null);

			// getSymbolStack().push(className);

		} catch (IndexOutOfBoundsException e) {
			ErrorHandler.printError("Too many arguments pass for method");
		}
		getCallStack().push(methodName);

	}

	public void assign() {
		Address s1 = getSs().pop();
		Address s2 = getSs().pop();
		// try {
		if (s1.varType != s2.varType) {
			ErrorHandler.printError("The type of operands in assign is different ");
		}
		// }catch (NullPointerException d)
		// {
		// d.printStackTrace();
		// }
		getMemory().add3AddressCode(Operation.ASSIGN, s1, s2, null);
	}

	public void add() {
		Address temp = Address.createTempAddress(getMemory(), varType.Int);
		Address s2 = getSs().pop();
		Address s1 = getSs().pop();

		if (s1.varType != varType.Int || s2.varType != varType.Int) {
			ErrorHandler.printError("In add two operands must be integer");
		}
		getMemory().add3AddressCode(Operation.ADD, s1, s2, temp);
		getSs().push(temp);
	}

	public void sub() {
		Address temp = Address.createTempAddress(getMemory(), varType.Int);
		Address s2 = getSs().pop();
		Address s1 = getSs().pop();
		if (s1.varType != varType.Int || s2.varType != varType.Int) {
			ErrorHandler.printError("In sub two operands must be integer");
		}
		getMemory().add3AddressCode(Operation.SUB, s1, s2, temp);
		getSs().push(temp);
	}

	public void mult() {
		Address temp = Address.createTempAddress(getMemory(), varType.Int);
		Address s2 = getSs().pop();
		Address s1 = getSs().pop();
		if (s1.varType != varType.Int || s2.varType != varType.Int) {
			ErrorHandler.printError("In mult two operands must be integer");
		}
		getMemory().add3AddressCode(Operation.MULT, s1, s2, temp);
		// getMemory().saveMemory();
		getSs().push(temp);
	}

	public void label() {
		getSs().push(new Address(getMemory().getCurrentCodeBlockAddress(), varType.Address));
	}

	public void save() {
		getSs().push(new Address(getMemory().saveMemory(), varType.Address));
	}

	public void _while() {
		getMemory().add3AddressCode(getSs().pop().num, Operation.JPF, getSs().pop(),
				new Address(getMemory().getCurrentCodeBlockAddress() + 1, varType.Address), null);
		getMemory().add3AddressCode(Operation.JP, getSs().pop(), null, null);
	}

	public void jpf_save() {
		Address save = new Address(getMemory().saveMemory(), varType.Address);
		getMemory().add3AddressCode(getSs().pop().num, Operation.JPF, getSs().pop(),
				new Address(getMemory().getCurrentCodeBlockAddress(), varType.Address), null);
		getSs().push(save);
	}

	public void jpHere() {
		getMemory().add3AddressCode(getSs().pop().num, Operation.JP,
				new Address(getMemory().getCurrentCodeBlockAddress(), varType.Address), null, null);
	}

	public void print() {
		getMemory().add3AddressCode(Operation.PRINT, getSs().pop(), null, null);
	}

	public void equal() {
		Address temp = Address.createTempAddress(getMemory(), varType.Bool);
		Address s2 = getSs().pop();
		Address s1 = getSs().pop();
		if (s1.varType != s2.varType) {
			ErrorHandler.printError("The type of operands in equal operator is different");
		}
		getMemory().add3AddressCode(Operation.EQ, s1, s2, temp);
		getSs().push(temp);
	}

	public void less_than() {
		Address temp = Address.createTempAddress(getMemory(), varType.Bool);
		Address s2 = getSs().pop();
		Address s1 = getSs().pop();
		if (s1.varType != varType.Int || s2.varType != varType.Int) {
			ErrorHandler.printError("The type of operands in less than operator is different");
		}
		getMemory().add3AddressCode(Operation.LT, s1, s2, temp);
		getSs().push(temp);
	}

	public void and() {
		Address temp = Address.createTempAddress(getMemory(), varType.Bool);
		Address s2 = getSs().pop();
		Address s1 = getSs().pop();
		if (s1.varType != varType.Bool || s2.varType != varType.Bool) {
			ErrorHandler.printError("In and operator the operands must be boolean");
		}
		getMemory().add3AddressCode(Operation.AND, s1, s2, temp);
		getSs().push(temp);
	}

	public void not() {
		Address temp = Address.createTempAddress(getMemory(), varType.Bool);
		Address s2 = getSs().pop();
		Address s1 = getSs().pop();
		if (s1.varType != varType.Bool) {
			ErrorHandler.printError("In not operator the operand must be boolean");
		}
		getMemory().add3AddressCode(Operation.NOT, s1, s2, temp);
		getSs().push(temp);
	}

	public void defClass() {
		getSs().pop();
		getSymbolTable().addClass(getSymbolStack().peek());
	}

	public void defMethod() {
		getSs().pop();
		String methodName = getSymbolStack().pop();
		String className = getSymbolStack().pop();

		getSymbolTable().addMethod(className, methodName, getMemory().getCurrentCodeBlockAddress());

		getSymbolStack().push(className);
		getSymbolStack().push(methodName);
	}

	public void popClass() {
		getSymbolStack().pop();
	}

	public void extend() {
		getSs().pop();
		getSymbolTable().setSuperClass(getSymbolStack().pop(), getSymbolStack().peek());
	}

	public void defField() {
		getSs().pop();
		getSymbolTable().addField(getSymbolStack().pop(), getSymbolStack().peek());
	}

	public void defVar() {
		getSs().pop();

		String var = getSymbolStack().pop();
		String methodName = getSymbolStack().pop();
		String className = getSymbolStack().pop();

		getSymbolTable().addMethodLocalVariable(className, methodName, var);

		getSymbolStack().push(className);
		getSymbolStack().push(methodName);
	}

	public void methodReturn() {
		// TODO : call ok

		String methodName = getSymbolStack().pop();
		Address s = getSs().pop();
		SymbolType t = getSymbolTable().getMethodReturnType(getSymbolStack().peek(), methodName);
		varType temp = varType.Int;
		switch (t) {
			case Int :
				break;
			case Bool :
				temp = varType.Bool;
		}
		if (s.varType != temp) {
			ErrorHandler.printError("The type of method and return address was not match");
		}
		getMemory().add3AddressCode(Operation.ASSIGN, s,
				new Address(getSymbolTable().getMethodReturnAddress(getSymbolStack().peek(), methodName),
						varType.Address, TypeAddress.Indirect),
				null);
		getMemory().add3AddressCode(Operation.JP,
				new Address(getSymbolTable().getMethodCallerAddress(getSymbolStack().peek(), methodName),
						varType.Address),
				null, null);

		// getSymbolStack().pop();
	}

	public void defParam() {
		// TODO : call Ok
		getSs().pop();
		String param = getSymbolStack().pop();
		String methodName = getSymbolStack().pop();
		String className = getSymbolStack().pop();

		getSymbolTable().addMethodParameter(className, methodName, param);

		getSymbolStack().push(className);
		getSymbolStack().push(methodName);
	}

	public void lastTypeBool() {
		getSymbolTable().setLastType(SymbolType.Bool);
	}

	public void lastTypeInt() {
		getSymbolTable().setLastType(SymbolType.Int);
	}

	public void main() {

	}
}
