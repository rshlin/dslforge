# DSL Forge

DSL Forge is a framework for the development of domain-specific language (DSL) editors for web applications and cloud IDEs. 
It allows to get web-based textual editors automatically from ANTLR grammars. From the language grammar specification, JavaScript parser and lexer are automatically generated and embedded into Cloud9’s ACE editor.

Check out the [live demo!](http://beeone.dslforge.org/workbench)

<p align="center">
  <img src="https://github.com/plugbee/dslforge/blob/master/plugins/org.dslforge.resources/online-workbench.png?raw=true" width="860"/>
</p>

The framework comes with two code generators which take the grammars expressed with Xtext or ANTLR and generate two variants of editors. 

- _**Generate Web Editor from ANTLR**_ produces a text editor with JavaScript parser and lexer from ANTLR v3. You get an ACE editor tailored for the language definition which runs fully on the client side. This editor has no backend, use it for integration with JavaScript servers.
- _**Generate Web Editor from Xtext**_ produces a plugin to be packaged with the original Xtext plugin into a workbench application enabling precise interaction between the client and the server.

<p align="center">
  <img src="https://github.com/plugbee/dslforge/blob/master/plugins/org.dslforge.resources/dslforge-generators.png?raw=true" width="460"/>
</p>

# Editor Features
Below the complete list of features bound by default in the generated editors:
- Syntax highlighting,
- Syntax validation,
- Content assist,
- Sever-side semantic validation,
- Scoping,
- Template proposals,
- Text hovering,
- Default Key bindings,
- Undo/Redo Support,
- Code Folding, 
- Brace Matching.

# Use Cases
Below some examples of use cases showing how the generated editors can be embedded into Web IDEs, forms, HTML5 canvas, guiders, and other JavaScript frameworks.

 - The following use case is a petrinet form made up of an embedded textual editor integrated with [D3.js](https://d3js.org/) framework. The form is available in the  [live demo](http://beeone.dslforge.org/workbench), open the petrinet project folder, right click on a petrinet model file, and select "Open with form editor".
 
<p align="center">
  <img src="https://github.com/plugbee/dslforge/blob/master/plugins/org.dslforge.resources/petrinet-form-editor.png?raw=true" width="620"/>
</p>

 - The following use case is an online coding game made with DSL Forge. It comes with a domain-specific language for specifying the actions of a robot, a remote debugger, next to a canvas animation made with [Phaser.js](https://phaser.io/). The game is free and publicly available on [CodingPark](http://www.codingpark.org).
 
<p align="center">
  <img src="https://github.com/plugbee/dslforge/blob/master/plugins/org.dslforge.resources/piraterobot-form-editor.png?raw=true" width="600"/>
</p>

 - The following example is an ANTLR v4 web editor embedded in the [live demo](http://beeone.dslforge.org/workbench), it demonstrates the integration of the editor with guiders based on the [Guider.js](https://github.com/pickhardt/Guiders-JS) framework.
 
 <p align="center">
  <img src="https://github.com/plugbee/dslforge/blob/master/plugins/org.dslforge.resources/antlr-v4-editor.png?raw=true" width="400"/>
</p>

## Installation

1. **Install Eclipse**
The current DSL Forge Tooling is based on Eclipse Neon [packages](http://www.eclipse.org/downloads/packages/).
To know which package is the most appropriate for you, select one of the following alternatives.

 - **Eclipse IDE for Java EE Developers**
You want a static web project with a full integration of ANTLR v3 (v4 is following) and ACE that you can easily debug in the browser. The recommended package in this case is Eclipse IDE for Java EE Developers as it comes with a simple HTTP Preview server easy to debug. The generator outputs the language mode and worker, and does the integration with the lexer/parser generated by ANTLR in the worker script. Once you update the grammar specification file, the parser/lexer are regenerated automatically on save, the language mode/worker are regenerated from contextual menu.

 - **Eclipse for RCP and RAP Developers**
You want a closer integration with Eclipse Modeling tools such as Xtext and EMF, trigger code generation, do live compilation and execute some generated artefacts, you’d better install Eclipse for RCP and RAP Developers. The tooling comes with a set of runtime plugins that will make the integration of your tool chains (e.g. transformations written in Xtend, Xpand, and Java) very easy. The editor is generated together with a *target platform* and a *build script*. The target platform is a super set of the [Basic Equinox Target](https://eclipse.org/rap/developers-guide/devguide.php?topic=target-install.html&version=2.0), it contains runtime plugins providing services such as workspace management, persistency, etc. The build script outputs a war bundle that you can deploy either in OSGI environments (Jetty) or JEE environments (Tomcat).

2. **Install DSL Forge Tooling**
Use the regular [update site](http://dslforge.org/downloads/tooling/repository/).

## Usage

The two usage scenarios are documented on the [website](http://www.dslforge.org).
- [Generate Web Editor from ANTLR](http://dslforge.org/getting-started-generate-ace-editor/)
- [Generate Web Editor from Xtext](http://dslforge.org/getting-started-generate-xtext-rap-editor/)

### Generate Web Editor from Xtext
- Step 1 - [Generate Web Editor from Xtext](https://youtu.be/vDGwmz4lU54)
- Step 2 - [Set up Debug Configuration and Test Editor](https://youtu.be/aD-DsFIwo3I)
- Step 3 - [Build Web Application Archive](https://youtu.be/FOnoayKri7E)
- Step 4 - [Deploy Web Archive on Servlet Container](https://youtu.be/2u5agv8koAo)

### Generate Web Editor from ANTLR
See [tutorial](http://dslforge.org/getting-started-generate-ace-editor/)

## Documentation

Additional usage informationn and technical details can be found below:
 - [Online Documentation](http://dslforge.org/documentation-2/)
 - [Modeling-Languages.com](http://modeling-languages.com/modeling-browser-dsl-forge/)
 - [EclipseCon France 2016](https://www.youtube.com/watch?v=PMiFgcwg3po)
 - [MoDELS 2014](http://ceur-ws.org/Vol-1255/paper5.pdf)

If you still need help, please open an [issue](https://github.com/plugbee/dslforge/issues/new) we'll try to help as much as possible in a reasonable time. 

If you need professional support, drop an email to team@dslforge.org.

## Contributing

We actively encourage and support contributions! DSL Forge is open source, feel free to fork the repository and enhance the tool the way you want. 

## License

DSL Forge program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0 which is available at http://www.eclipse.org/legal/epl-v10.html
