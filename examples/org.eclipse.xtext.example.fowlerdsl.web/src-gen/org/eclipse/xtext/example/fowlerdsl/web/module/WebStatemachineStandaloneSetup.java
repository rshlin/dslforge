/**
 * @Generated
 */
package org.eclipse.xtext.example.fowlerdsl.web.module;

import org.eclipse.xtext.util.Modules2;
import org.eclipse.xtext.example.fowlerdsl.StatemachineRuntimeModule;
import org.eclipse.xtext.example.fowlerdsl.StatemachineStandaloneSetup;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class WebStatemachineStandaloneSetup extends StatemachineStandaloneSetup {

	public static void doSetup() {
		new StatemachineStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	public Injector createInjector(String language) {
		try {
			Module runtimeModule = getRuntimeModule();
			Injector injector = Guice.createInjector(runtimeModule);
			register(injector);
			return injector;
		} catch (Exception e) {
			System.err.println("Failed to create injector for " + language);
			throw new RuntimeException("Failed to create injector for "
					+ language, e);
		}
	}

	private Module getRuntimeModule() {
		org.eclipse.xtext.common.TerminalsStandaloneSetup.doSetup();
		StatemachineRuntimeModule original = new StatemachineRuntimeModule();
		WebStatemachineRuntimeModule module = new WebStatemachineRuntimeModule();
		Module mergedModule = Modules2.mixin((Module) original, module);
		return mergedModule;
	}
}
