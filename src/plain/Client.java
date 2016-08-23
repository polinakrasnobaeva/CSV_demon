package plain;

public class Client {
	private String webSite;
	private String name;
	
	public Client(String website, String name){
		this.setWebSite(website);
		this.setName(name);
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return this.webSite;
	}
}
