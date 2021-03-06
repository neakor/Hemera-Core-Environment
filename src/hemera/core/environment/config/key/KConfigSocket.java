package hemera.core.environment.config.key;

/**
 * <code>KConfigSocket</code> defines the enumerations
 * of all the XML tags used in the socket section of the
 * configuration file.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum KConfigSocket {
	/**
	 * The socket configuration tag.
	 */
	Root("socket"),
	/**
	 * The socket port tag.
	 */
	Port("port"),
	/**
	 * The socket timeout tag.
	 */
	Timeout("timeout"),
	/**
	 * The socket buffer size tag.
	 */
	BufferSize("buffer-size"),
	/**
	 * The optional certificate file path used for SSL
	 * server connection.
	 */
	Certificate("cert"),
	/**
	 * The optional certificate public key password.
	 */
	KeyPassword("key-password");
	
	/**
	 * The <code>String</code> tag.
	 */
	public final String tag;
	
	/**
	 * Constructor of <code>KConfigSocket</code>.
	 * @param tag The <code>String</code> tag.
	 */
	private KConfigSocket(final String tag) {
		this.tag = tag;
	}
}
