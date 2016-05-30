package com.feed.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.feed.modal.Tweet;
import com.feed.modal.User;

/**
 * Simulates Actual Database
 * 
 * @author rupalph
 *
 */
public class TweetDAO {

	private static final int DEFAULT_CACHE_SIZE=3;
	//simulates in-memory db
	private static ConcurrentMap<String,List<Tweet>> myDb=new ConcurrentHashMap<String, List<Tweet>>();
	
	
	//simulates all-users in the system created so far
	private static ConcurrentMap<String,User>	allUsers=new ConcurrentHashMap<String,User>();
	
	//create some dummy data
	static {
		User u1=new User("1000");
		User u2=new User("1001");
		User u3=new User("1002");
		User u4=new User("1003");
		User u5=new User("1004");
		u1.addFollower(u2);
		u1.addFollower(u3);
		u2.addFollower(u1);
		u4.addFollower(u1);
		u5.addFollower(u3);
		u3.addFollower(u1);
		u3.addFollower(u2);
		allUsers.put("1000", u1);
		allUsers.put("1001", u2);
		allUsers.put("1002", u3);
		allUsers.put("1003", u4);
		allUsers.put("1004", u5);
		
		Tweet t1=new Tweet.TweetBuilder("1000", "my new car").build();
		saveTweet(t1);
		
		Tweet t2=new Tweet.TweetBuilder("1000", "lets go to park").build();
		saveTweet(t2);
		
		Tweet t3=new Tweet.TweetBuilder("1000", "hello").build();
		saveTweet(t3);
		
		Tweet t4=new Tweet.TweetBuilder("1003", "fire").build();
		saveTweet(t4);
		Tweet t5=new Tweet.TweetBuilder("1003", "fireman is here").build();
		saveTweet(t5);
		
		Tweet t6=new Tweet.TweetBuilder("1003", "message3").build();
		saveTweet(t6);
		
		
	}
	
	/**
	 * save tweet to database
	 * @param tweet
	 */
	public static void saveTweet(Tweet tweet) {
		System.out.println("in savetweet");
		String userid=tweet.getUserid();
		
		if(myDb.containsKey(userid))
		{
			List<Tweet> list=myDb.get(userid);
			if(list==null)
				list=new ArrayList<Tweet>();
			list.add(tweet);
			
			
		}
		else {
			List<Tweet> list=new ArrayList<Tweet>();
			list.add(tweet);
			myDb.put(userid,list);
			

		}
		AppCache.addToCache(userid, tweet);
		//debug
		//System.out.println("cache"+cache.toString());
		System.out.println("db:"+myDb.toString());
	}
	
	/**
	 * Get recent tweets
	 * 
	 * @param userid
	 * @return
	 */
	public static List<Tweet> getRecentTweets(String userid)
	{
		
		
		System.out.println("TweetDAO:getRecentTweets");
		List<Tweet> list = new ArrayList<Tweet>();
		if(AppCache.hasUserId(userid))
			list = AppCache.getTweets(userid);
		else {
			list = getRecentTweetsFromDB(userid);
			list = AppCache.addToCache(userid,list);
		}
		System.out.println("TweetDAO getRecentTWeets:"+list);
		return list;
	}
	/**
	 * Fetches recent tweets from cache
	 * 
	 * @param userid
	 * @return
	 */
	public static List<Tweet> getRecentTweetsFromDB(String userid)
	{
		//System.out.println(cache.toString());
		List<Tweet> list=new ArrayList<Tweet>();
		if(myDb.containsKey(userid))
			list= myDb.get(userid);
		System.out.println("TweetDAO getRecentTWeetsFromDB:"+list);
		return list;
	}

	/**
	 * Finds user by userid
	 * @param userid
	 * @return
	 */
	public static User findUserById(String userid) {
		return allUsers.get(userid);
	}
	
	/**
	 * 
	 * @param userid
	 * @return
	 */
	public static User addUser(String userid) {
		User u=new User(userid);
		
		return allUsers.put(userid, u);
	}
	/**
	 * 
	 * @param userid
	 * @param followUser
	 */
	public static void addFollower(String userid,String followUser) {
		User u= allUsers.get(userid);
		User follower=new User(followUser);
		u.addFollower(follower);
	}

	public static void resetDb() {
		myDb.clear();
		
	}
}
