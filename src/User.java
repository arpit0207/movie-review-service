import java.util.HashSet;
import java.util.Set;

enum UserType
{
	VIEWER("Viewer", 1),
	CRITIC("Critic", 2);
	
	private final String label;
	private final int multiplier;

    private UserType(String label, int multiplier) 
    {
        this.label = label;
        this.multiplier = multiplier;
    }
    
    @Override 
    public String toString()
    { 
        return this.label; 
    }
    
    public int getMultiplier()
    {
    	return multiplier;
    }
}

public class User {
	String uName;
	UserType type;
	int numOfReviews;
	Set<String> movieReviewed;
	
	public User(String uName)
	{
		this.uName = uName;
		type = UserType.VIEWER;
		numOfReviews = 0;
		movieReviewed = new HashSet<String>();
	}
	
	// Considering each user will have different user name
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj == null || !(obj instanceof User))
			return false;
		
		return this.uName.equals(((User)obj).uName);
	}
	
	@Override
	public int hashCode()
	{
		int result = 17;
		result = 31 * result + uName.hashCode();
		return result;
	}
	
	boolean addMovieReviewed(String mName)
	{
		if(!movieReviewed.contains(mName))
		{
			movieReviewed.add(mName);
			numOfReviews++;
			if(numOfReviews == 3)
			{
				type = UserType.CRITIC;
			}
			
			return true;
		}
		
		return false;
	}

}
