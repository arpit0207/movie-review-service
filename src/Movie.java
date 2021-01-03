import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

enum Genre
{
	DRAMA("Drama"),
	ROMANCE("Romance"),
	SUSPENSE("Suspense"),
	ACTION("Action"),
	COMEDY("Comedy"),
	FICTION("Fiction");
	
	public final String label;

    private Genre(String label) {
        this.label = label;
    }
    
    @Override 
    public String toString() { 
        return this.label; 
    }
}

public class Movie {
	String mName;
	int yearReleased;
	Set<Genre> genre;
	// List - first score, second numOfUser - assumed rating will be Integer only b/w 1 to 10
	HashMap<UserType, List<Integer>> ratings;
	
	public Movie(String mName, int yearReleased, List<Genre> genre)
	{
		this.mName = mName;
		this.yearReleased = yearReleased;
		this.genre = new HashSet<Genre>(genre);
		ratings = new HashMap<UserType, List<Integer>>();
	}
	
	// Considering each movie will have different movie name
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj == null || !(obj instanceof User))
			return false;
		
		return this.mName.equals(((Movie)obj).mName);
	}
	
	@Override
	public int hashCode()
	{
		int result = 17;
        result = 31 * result + mName.hashCode();
        
        return result;
	}
	
	public void addReviewForMovie(UserType uType, int rating)
	{
		List<Integer> list = ratings.getOrDefault(uType, new ArrayList<Integer>());
		if(list.isEmpty())
		{
			list.add(rating);
			list.add(1);
		}
		else
		{
			list.set(0, list.get(0)+rating);
			list.set(1, list.get(1)+1);
		}
		ratings.put(uType, list);
	}
}
