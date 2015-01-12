package bluepumpkin.domain;

public class WebOrderStatus {
	private String status;
	private boolean selected;
	
public WebOrderStatus() {
}	
	
public WebOrderStatus(String status, boolean selected) {
		super();
		this.status = status;
		this.selected = selected;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
