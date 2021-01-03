import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class MovieReviewSystem {
	
	static class MovieToRating implements Comparable<MovieToRating>{
		String mName;
		double rating;
		
		public MovieToRating(String mName, double rating)
		{
			this.mName = mName;
			this.rating = rating;
		}
		
		public int compareTo(MovieToRating m)
		{
			if(m.rating > this.rating)
				return 1;
			
			if(m.rating < this.rating)
				return -1;
			
			return 0;
		}
	}
	
	static Map<String, User> userMap;
	static Map<String, Movie> movieMap;

	public static void main(String[] args) {
		
		userMap = new HashMap<String, User>();
		movieMap = new HashMap<String, Movie>();
		
		addUser("SRK");
		addUser("Salman");
		addUser("Deepika");
		
		addMovie("Don", 2006, Arrays.asList(new Genre[] {Genre.ACTION, Genre.COMEDY}));
		addMovie("Tiger", 2008, Arrays.asList(new Genre[] {Genre.DRAMA}));
		addMovie("Padmavat", 2006, Arrays.asList(new Genre[] {Genre.COMEDY}));
		addMovie("Lunchbox", 2022, Arrays.asList(new Genre[] {Genre.DRAMA}));
		addMovie("Guru", 2006, Arrays.asList(new Genre[] {Genre.DRAMA}));
		addMovie("Metro", 2006, Arrays.asList(new Genre[] {Genre.ROMANCE}));
		addMovie("Rock On", 2008, Arrays.asList(new Genre[] {Genre.DRAMA}));
		addMovie("Tashan", 2008, Arrays.asList(new Genre[] {Genre.ACTION, Genre.COMEDY}));
		addMovie("Sooryavanshi", 2022, Arrays.asList(new Genre[] {Genre.ACTION, Genre.COMEDY}));
		addMovie("Badla", 2022, Arrays.asList(new Genre[] {Genre.SUSPENSE}));
		
		addReview("SRK", "Don", 2);
		addReview("SRK", "Padmavat", 8);
		addReview("Salman", "Don", 5);
		addReview("Deepika", "Don", 9);
		addReview("Deepika", "Guru", 6);
		addReview("SRK","Don", 10); //Movie already reviewed by User
		addReview("Deepika", "Lunchbox", 5); //Movie not released
		addReview("SRK", "Tiger", 5);
		addReview("SRK", "Metro", 7); //SRK will review this movie as Critic
		
		topMovieByReviewScoreYear(1, 2006, UserType.VIEWER);
		topMovieByReviewScoreYear(1, 2006, UserType.CRITIC);
		topMovieByReviewScoreGenre(1, Genre.DRAMA, UserType.VIEWER);
		
		avgReviewScoreByYear(2006);
		
	}
	
	public static void addUser(String uName)
	{
		User newUser = new User(uName);
		if(userMap.containsKey(uName))
		{
			System.out.println("User already exists with username: " + uName);
			return;
		}
		userMap.put(uName, newUser);
		System.out.println("User added with username: " + uName);
	}
	
	public static void addMovie(String mName, int yearReleased, List<Genre> genre)
	{
		Movie newMovie = new Movie(mName, yearReleased, genre);
		if(movieMap.containsKey(mName))
		{
			System.out.println("Movie already exists with moviename: " + mName);
			return;
		}
		movieMap.put(mName, newMovie);
		System.out.println("Movie added with moviename: " + mName);
		
	}
	
	public static void addReview(String uName, String mName, int rating)
	{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		User user = userMap.get(uName);
		Movie movie = movieMap.get(mName);
		
		if(user == null)
		{
			System.out.println("no user found");
			return;
		}
		
		if(movie == null)
		{
			System.out.println("no movie found");
			return;
		}
		
		if(movie.yearReleased > year)
		{
			System.out.println("Movie not released");
			return;
		}
		
		if(rating < 1 || rating > 10)
		{
			System.out.println("Enter valid rating");
			return;
		}
		
		if(user.movieReviewed.contains(mName))
		{
			System.out.println("Movie already reviewed by User");
			return;
		}
		
		user.addMovieReviewed(mName);
		movie.addReviewForMovie(user.type, rating);
		
		System.out.println("Review added for movie " + mName + " by user " + uName);
		
	}
	
	public static void topMovieByReviewScoreYear(int numOfMovies, int year, UserType uType)
	{
		PriorityQueue<MovieToRating> pq = new PriorityQueue<MovieToRating>();
		
		for(Map.Entry<String, Movie> entry: movieMap.entrySet())
		{
			long score = 0;
			Movie movie = entry.getValue();
			if(movie.yearReleased == year)
			{
				for(Map.Entry<UserType, List<Integer>> rating: movie.ratings.entrySet())
				{
					if(rating.getKey() == uType)
					{
						score += rating.getValue().get(0)*rating.getKey().getMultiplier();
					}
				}
			}
			// Considering only reviewed movies
			if(score > 0)
				pq.add(new MovieToRating(movie.mName, score));
		}
		
		if(pq.isEmpty())
		{
			System.out.println("No movie reviewed for year " + year + " by " + uType);
			return;
		}
		
		System.out.println("Top movies by review score for year " + year + " by " + uType);
		while(!pq.isEmpty() && numOfMovies > 0)
		{
			numOfMovies--;
			MovieToRating tmp = pq.poll();
			System.out.println(tmp.mName + " - " + (int)tmp.rating);
		}
		
	}
	
	public static void topMovieByReviewScoreGenre(int numOfMovies, Genre genre, UserType uType)
	{
		PriorityQueue<MovieToRating> pq = new PriorityQueue<MovieToRating>();
		
		for(Map.Entry<String, Movie> entry: movieMap.entrySet())
		{
			long score = 0;
			Movie movie = entry.getValue();
			if(movie.genre.contains(genre))
			{
				for(Map.Entry<UserType, List<Integer>> rating: movie.ratings.entrySet())
				{
					if(rating.getKey() == uType)
					{
						score += rating.getValue().get(0)*rating.getKey().getMultiplier();
					}
				}
			}
			// Considering only reviewed movies
			if(score > 0)
				pq.add(new MovieToRating(movie.mName, score));
		}
		
		if(pq.isEmpty())
		{
			System.out.println("No movies reviewed for genre " + genre + " by " + uType);
			return;
		}
		
		System.out.println("Top movies by review score for genre " + genre + " by " + uType);
		while(!pq.isEmpty() && numOfMovies > 0)
		{
			MovieToRating tmp = pq.poll();
			System.out.println(tmp.mName + " - " + (int)tmp.rating);
		}
	}
	
	public static void avgReviewScoreByYear(int year)
	{
		double count = 0, score = 0;
		for(Map.Entry<String, Movie> entry: movieMap.entrySet())
		{
			Movie movie = entry.getValue();
			if(movie.yearReleased == year)
			{
				for(Map.Entry<UserType, List<Integer>> rating: movie.ratings.entrySet())
				{
					score += rating.getValue().get(0)*rating.getKey().getMultiplier();
					count += rating.getValue().get(1);
				}
			}
		}
		
		if(count == 0 || score == 0)
		{
			System.out.println("No movies reviewed for this year");
			return;
		}
		
		System.out.println("Average review score for the year " + year + " : " + String.format("%.2f", score/count));
	}
	
	public static void avgReviewScoreByGenre(Genre genre)
	{
		double count = 0, score = 0;
		for(Map.Entry<String, Movie> entry: movieMap.entrySet())
		{
			Movie movie = entry.getValue();
			if(movie.genre.contains(genre))
			{
				for(Map.Entry<UserType, List<Integer>> rating: movie.ratings.entrySet())
				{
					score += rating.getValue().get(0)*rating.getKey().getMultiplier();
					count += rating.getValue().get(1);
				}
			}
		}
		
		if(count == 0 || score == 0)
		{
			System.out.println("No movies reviewed of this genre");
			return;
		}
		
		System.out.println("Average review score for the genre " + genre + " : " + String.format("%.2f", score/count));
	}
	
	public static double avgReviewScoreOfMovie(String mName)
	{
		Movie movie = movieMap.get(mName);
		double count = 0, score = 0;
		for(Map.Entry<UserType, List<Integer>> rating: movie.ratings.entrySet())
		{
			score += rating.getValue().get(0)*rating.getKey().getMultiplier();
			count += rating.getValue().get(1);
		}
		
		if(count == 0 || score == 0)
		{
			System.out.println("This movie is not reviewed");
			return 0;
		}
		
		double avg = score/count;
		System.out.println("Average review score of the movie " + mName + " : " + String.format("%.2f", avg));
		return avg;
	}
	
	
	/* Additional Function */
	public static void topMovieAvgReviewScoreByYear(int year)
	{
		PriorityQueue<MovieToRating> pq = new PriorityQueue<MovieToRating>();
		for(Map.Entry<String, Movie> entry: movieMap.entrySet())
		{
			Movie movie = entry.getValue();
			if(movie.yearReleased == year)
			{
				double avgRating = avgReviewScoreOfMovie(movie.mName);
				if(avgRating > 0)
				{
					pq.add(new MovieToRating(movie.mName, avgRating));
				}
			}
		}
		
		if(!pq.isEmpty())
		{
			MovieToRating tmp = pq.poll();
			System.out.println("Top movie by average review score in " + year + " year: " + tmp.mName + " with Rating " + String.format("%.2f", tmp.rating));
		}
		else
		{
			System.out.println("No movie rated in " + year + " year");
		}
	}
	
	public static void topMovieAvgReviewScoreByGenre(Genre genre)
	{
		PriorityQueue<MovieToRating> pq = new PriorityQueue<MovieToRating>();
		for(Map.Entry<String, Movie> entry: movieMap.entrySet())
		{
			Movie movie = entry.getValue();
			if(movie.genre.contains(genre))
			{
				double avgRating = avgReviewScoreOfMovie(movie.mName);
				if(avgRating > 0)
				{
					pq.add(new MovieToRating(movie.mName, avgRating));
				}
			}
		}
		
		if(!pq.isEmpty())
		{
			MovieToRating tmp = pq.poll();
			System.out.println("Top movie by average review score in " + genre + " genre: " + tmp.mName + " with Rating " + String.format("%.2f", tmp.rating));
		}
		else
		{
			System.out.println("No movie rated in " + genre + " genre");
		}
	}
	
}
