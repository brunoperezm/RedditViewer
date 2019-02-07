package com.example.brpper.redditviewer;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

@Entity()
public class RedditSubredditDB {
	@Id(autoincrement = true)
	private Long id;

	@Property
	private String name;

	@ToMany(referencedJoinProperty = "subredditId")
	private List<RedditPostDB> post;

	/** Used to resolve relations */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	/** Used for active entity operations. */
	@Generated(hash = 809747485)
	private transient RedditSubredditDBDao myDao;

	@Generated(hash = 1347802842)
	public RedditSubredditDB(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Generated(hash = 56822811)
	public RedditSubredditDB() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 128553479)
	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 1942392019)
	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 713229351)
	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}

	/**
	 * To-many relationship, resolved on first access (and after reset).
	 * Changes to to-many relations are not persisted, make changes to the target entity.
	 */
	@Generated(hash = 1555136312)
	public List<RedditPostDB> getPost() {
		if (post == null) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			RedditPostDBDao targetDao = daoSession.getRedditPostDBDao();
			List<RedditPostDB> postNew = targetDao._queryRedditSubredditDB_Post(id);
			synchronized (this) {
				if (post == null) {
					post = postNew;
				}
			}
		}
		return post;
	}

	/** Resets a to-many relationship, making the next get call to query for a fresh result. */
	@Generated(hash = 687148032)
	public synchronized void resetPost() {
		post = null;
	}

	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 404300907)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getRedditSubredditDBDao() : null;
	}
}
