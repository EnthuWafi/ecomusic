package com.enth.ecomusic.util;

import com.enth.ecomusic.service.GenreCacheService;
import com.enth.ecomusic.service.LikeService;
import com.enth.ecomusic.service.MailService;
import com.enth.ecomusic.service.MoodCacheService;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.service.PlayHistoryService;
import com.enth.ecomusic.service.PlaylistService;
import com.enth.ecomusic.service.ReportService;
import com.enth.ecomusic.service.RoleCacheService;
import com.enth.ecomusic.service.StripeService;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.service.UserService;

public final class AppContext {

    // Caches (shared)
    private final RoleCacheService roleCacheService;
    private final GenreCacheService genreCacheService;
    private final MoodCacheService moodCacheService;

    // Core services
    private final UserService userService;
    private final MusicService musicService;
    private final PlaylistService playlistService;
    private final SubscriptionService subscriptionService;
    private final LikeService likeService;
    private final PlayHistoryService playHistoryService;
    private final StripeService stripeService;
    private final ReportService reportService;
    private final MailService mailService;

    public AppContext() {
        // Cache first
        this.roleCacheService = new RoleCacheService();
        this.genreCacheService = new GenreCacheService();
        this.moodCacheService = new MoodCacheService();

        this.userService = new UserService(roleCacheService);
        this.musicService = new MusicService(userService, genreCacheService, moodCacheService);
        this.playlistService = new PlaylistService(musicService, userService);
        this.subscriptionService = new SubscriptionService(userService, musicService);
        this.likeService = new LikeService(musicService);
        this.playHistoryService = new PlayHistoryService(musicService);
        this.stripeService = new StripeService(subscriptionService);
        this.reportService = new ReportService(subscriptionService, userService, musicService);
        this.mailService = new MailService("smtp.gmail.com", 587, AppConfig.get("mailUsername"), AppConfig.get("mailAppPassword"));
    }

    public UserService getUserService() {
        return userService;
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public PlaylistService getPlaylistService() {
        return playlistService;
    }

    public RoleCacheService getRoleCacheService() {
        return roleCacheService;
    }

    public GenreCacheService getGenreCacheService() {
        return genreCacheService;
    }

    public MoodCacheService getMoodCacheService() {
        return moodCacheService;
    }

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public LikeService getLikeService() {
		return likeService;
	}

	public PlayHistoryService getPlayHistoryService() {
		return playHistoryService;
	}

	public StripeService getStripeService() {
		return stripeService;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public MailService getMailService() {
		return mailService;
	}
	
	
    
    
}
