package hemera.core.environment.ham;

import hemera.core.environment.hbm.HBMModule;
import hemera.core.utility.FileUtils;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <code>HAMModule</code> defines the immutable unit
 * representing a single module node in a HAM file.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public class HAMModule {
	/**
	 * The <code>String</code> fully qualified module
	 * class name.
	 */
	public final String classname;
	/**
	 * The <code>String</code> optional module
	 * configuration file path.
	 */
	public final String configFile;
	/**
	 * The <code>String</code> optional module resources
	 * directory.
	 */
	public final String resourcesDir;

	/**
	 * Constructor of <code>HAMModule</code>.
	 * @param node The <code>Element</code> XML node.
	 */
	HAMModule(final Element node) {
		// Verify tag name.
		final String tagname = node.getTagName();
		if (!tagname.equalsIgnoreCase(KHAM.Module.tag)) {
			throw new IllegalArgumentException("Invalid module tag: " + tagname);
		}
		// Class name tag.
		final NodeList classList = node.getElementsByTagName(KHAM.ModuleClassname.tag);
		if (classList == null || classList.getLength() != 1) {
			throw new IllegalArgumentException("Invalid module tag. Each module tag must contain one class name tag.");
		}
		this.classname = classList.item(0).getTextContent();
		// Optional configuration file.
		final NodeList configlist = node.getElementsByTagName(KHAM.ModuleConfigFile.tag);
		if (configlist == null || configlist.getLength() <= 0) this.configFile = null;
		else this.configFile = configlist.item(0).getTextContent();
		// Optional resources directory.
		final NodeList resourcesList = node.getElementsByTagName(KHAM.ModuleResourceDir.tag);
		if (resourcesList == null || resourcesList.getLength() <= 0) this.resourcesDir = null;
		else this.resourcesDir = resourcesList.item(0).getTextContent();
	}

	/**
	 * Constructor of <code>HAMModule</code>.
	 * <p>
	 * This constructor creates a HAM module node based
	 * on the given HBM module.
	 * @param hbmModule The <code>HBMModule</code>.
	 */
	HAMModule(final HBMModule hbmModule) {
		this.classname = hbmModule.classname;
		this.configFile = hbmModule.configFile;
		this.resourcesDir = hbmModule.resourcesDir;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof HAMModule) {
			final HAMModule given = (HAMModule)o;
			final boolean classname = this.classname.equals(given.classname);
			final boolean config = (this.configFile==null) ? (given.configFile==null) : this.configFile.equals(given.configFile);
			return (classname && config);
		}
		return false;
	}

	/**
	 * Convert this HAM module to a XML element.
	 * @param document The <code>Document</code> to
	 * create the new tags from.
	 * @return The module <code>Element</code>.
	 */
	public Element toXML(final Document document) {
		final Element module = document.createElement(KHAM.Module.tag);
		// Class-name tag.
		final Element classname = document.createElement(KHAM.ModuleClassname.tag);
		classname.setTextContent(this.classname);
		module.appendChild(classname);
		// Configuration file tag.
		if (this.configFile != null && this.configFile.length() > 0) {
			// Retrieve the configuration file name.
			final int index = this.configFile.lastIndexOf(File.separator)+1;
			final String configFileName = this.configFile.substring(index);
			// Build configuration file path after deployment.
			// HOME/apps/APPLICATION/MODULE/CONFIG.FILE
			final Element config = document.createElement(KHAM.ModuleConfigFile.tag);
			final StringBuilder builder = new StringBuilder();
			builder.append(KHAM.PlaceholderAppsDir.tag);
			builder.append(this.classname).append(File.separator);
			builder.append(configFileName);
			config.setTextContent(builder.toString());
			module.appendChild(config);
		}
		// Resources directory.
		if (this.resourcesDir != null && this.resourcesDir.length() > 0) {
			// Build resources directory after deployment.
			// HOME/apps/APPLICATION/MODULE/resources/
			final Element resources = document.createElement(KHAM.ModuleResourceDir.tag);
			final StringBuilder builder = new StringBuilder();
			builder.append(KHAM.PlaceholderAppsDir.tag);
			builder.append(this.classname).append(File.separator);
			builder.append("resources");
			resources.setTextContent(FileUtils.instance.getValidDir(builder.toString()));
			module.appendChild(resources);
		}
		return module;
	}
}