import { PlaylistChooseModal } from "./PlaylistChooseModal.js";

export const MusicPlayer = ({ baseUrl, musicId, isAdmin = false, userId }) => {
	const [musicData, setMusicData] = React.useState(null);
	const [isLoading, setIsLoading] = React.useState(false);
	const [isPlaying, setIsPlaying] = React.useState(false);
	const [currentTime, setCurrentTime] = React.useState(0);
	const [duration, setDuration] = React.useState(0);
	const [volume, setVolume] = React.useState(0.7);
	const [isLiked, setIsLiked] = React.useState(false);
	const [hasRecordedPlay, setHasRecordedPlay] = React.useState(false);
	const [listeningTime, setListeningTime] = React.useState(0);
	const [showPlaylistModal, setShowPlaylistModal] = React.useState(false);

	const hasAccessRef = React.useRef(false);
	const wavesurferRef = React.useRef(null);
	const containerRef = React.useRef(null);
	const listeningTimeRef = React.useRef(0);
	const intervalRef = React.useRef(null);
	const hasRecordedPlayRef = React.useRef(false);
	const currentTimeRef = React.useRef(0);
	const durationRef = React.useRef(0);
	const progressBarRef = React.useRef();
	const isLikedRef = React.useRef(false);

	// Keep refs in sync with state
	React.useEffect(() => {
		isLikedRef.current = isLiked;
	}, [isLiked]);

	React.useEffect(() => {
		currentTimeRef.current = currentTime;
	}, [currentTime]);

	React.useEffect(() => {
		durationRef.current = duration;
	}, [duration]);

	React.useEffect(() => {
		listeningTimeRef.current = listeningTime;
	}, [listeningTime]);

	React.useEffect(() => {
		hasRecordedPlayRef.current = hasRecordedPlay;
	}, [hasRecordedPlay]);

	// Initialize WaveSurfer
	React.useEffect(() => {
		if (typeof window !== 'undefined' && window.WaveSurfer && containerRef.current) {
			wavesurferRef.current = window.WaveSurfer.create({
				container: containerRef.current,
				waveColor: 'rgb(200, 0, 200)',
				progressColor: 'rgb(100, 0, 100)',
				barWidth: 2,
				barGap: 1,
				barRadius: 2,
				cursorColor: '#0d6efd',
				responsive: true,
				height: 60,
				autoplay: false,
				mediaControls: false,
				normalize: true,
				interact: true,
				dragToSeek: true
			});

			// Event listeners
			wavesurferRef.current.on('ready', () => {
				setDuration(wavesurferRef.current.getDuration());
				wavesurferRef.current.setVolume(volume);
			});

			wavesurferRef.current.on('audioprocess', () => {
				setCurrentTime(wavesurferRef.current.getCurrentTime());
			});

			wavesurferRef.current.on('play', () => {
				setIsPlaying(true);
				startTrackingTime();
			});

			wavesurferRef.current.on('pause', () => {
				setIsPlaying(false);
				stopTrackingTime();
			});

			wavesurferRef.current.on('finish', () => {
				setIsPlaying(false);
				setCurrentTime(0);
				stopTrackingTime();
				sendPlayRecord();
			});
		}

		return () => {
			if (wavesurferRef.current) {
				wavesurferRef.current.destroy();
			}
		};
	}, []);

	React.useEffect(() => {
		let animationFrameId;

		const updateProgressBar = () => {
			if (progressBarRef.current && wavesurferRef.current && durationRef.current > 0) {
				const current = wavesurferRef.current.getCurrentTime();
				const percent = (current / durationRef.current) * 100;
				progressBarRef.current.style.width = `${percent}%`;
			}
			animationFrameId = requestAnimationFrame(updateProgressBar);
		};

		animationFrameId = requestAnimationFrame(updateProgressBar);

		return () => cancelAnimationFrame(animationFrameId);
	}, []);

	// Update volume when it changes
	React.useEffect(() => {
		if (wavesurferRef.current) {
			wavesurferRef.current.setVolume(volume);
		}
	}, [volume]);

	// Load music data and audio
	React.useEffect(() => {
		const loadMusic = async () => {
			if (!baseUrl || !musicId) return;

			setIsLoading(true);
			// Reset tracking state for new music
			setHasRecordedPlay(false);
			setListeningTime(0);
			hasRecordedPlayRef.current = false;
			listeningTimeRef.current = 0;

			try {
				const response = await fetch(`${baseUrl}/api/music/${musicId}`);
				const data = await response.json();

				if (data.success) {
					setMusicData(data.data.results);

					if (wavesurferRef.current) {
						const audioUrl = `${baseUrl}/stream/audio/${musicId}`;
						try {
							await wavesurferRef.current.load(audioUrl);
							hasAccessRef.current = true;
						} catch (err) {
							console.warn("Unable to load audio stream:", err);
							window.toastr.warning("You do not have access to this track.");
							hasAccessRef.current = false;
						}
					}
				}
			} catch (error) {
				console.error('Error loading music:', error);
			} finally {
				setIsLoading(false);
			}
		};

		loadMusic();
	}, [baseUrl, musicId]);

	// Check if music is liked
	React.useEffect(() => {
		const checkIfLiked = async () => {
			if (!baseUrl || !musicId) return;

			try {
				const response = await fetch(`${baseUrl}/api/like/${musicId}`);
				if (response.ok) {
					const data = await response.json();
					setIsLiked(data.data === true);
				} else {
					console.warn("Failed to fetch like status");
				}
			} catch (error) {
				console.error('Error checking like status:', error);
			}
		};

		checkIfLiked();
	}, [baseUrl, musicId]);

	const startTrackingTime = () => {
		// Don't start if already tracking or play already recorded
		if (intervalRef.current || hasRecordedPlayRef.current) {
			console.log('Not starting tracking - already tracking or play recorded');
			return;
		}

		console.log('Starting time tracking');
		const id = setInterval(() => {
			// Check if we should stop tracking
			if (hasRecordedPlayRef.current) {
				console.log('Stopping tracking - play recorded');
				clearInterval(id);
				intervalRef.current = null;
				return;
			}

			setListeningTime(prev => {
				const newTime = prev + 1;
				return newTime;
			});
		}, 1000);

		intervalRef.current = id;
	};

	const stopTrackingTime = () => {
		if (intervalRef.current) {
			console.log('Stopping time tracking');
			clearInterval(intervalRef.current);
			intervalRef.current = null;
		}
	};

	// Clean up on unmount and handle page unload
	React.useEffect(() => {
		const handleUnload = () => {
			console.log('Page unloading, sending play record');
			if (listeningTimeRef.current > 5 && !hasRecordedPlayRef.current && hasAccessRef.current) {
				const durationMs = listeningTimeRef.current * 1000;
				const wasSkipped = listeningTimeRef.current < (durationRef.current * 0.8);

				if (navigator.sendBeacon && baseUrl && musicId) {
					const data = JSON.stringify({
						listenDuration: durationMs,
						wasSkipped: wasSkipped
					});
					navigator.sendBeacon(`${baseUrl}/api/play/${musicId}`, data);
				}
			}
		};

		window.addEventListener('beforeunload', handleUnload);

		return () => {
			window.removeEventListener('beforeunload', handleUnload);
			// Clean up interval on unmount
			if (intervalRef.current) {
				clearInterval(intervalRef.current);
				intervalRef.current = null;
			}
		};
	}, [baseUrl, musicId]);

	// Record play function
	const sendPlayRecord = async () => {
		const listening = listeningTimeRef.current;
		const hasPlayed = hasRecordedPlayRef.current;
		const access = hasAccessRef.current;

		console.log('sendPlayRecord called', {
			hasRecordedPlay: hasPlayed,
			listeningTime: listening,
			hasAccess: access
		});

		if (!(listening > 0 && !hasPlayed && access)) {
			console.log('Skipping play record - already recorded or no listening time');
			return;
		}

		// Stop tracking time immediately
		stopTrackingTime();

		const durationMs = listening * 1000;
		const wasSkipped = listeningTimeRef.current < (durationRef.current * 0.8);

		try {
			const response = await fetch(`${baseUrl}/api/play/${musicId}`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({
					listenDuration: durationMs,
					wasSkipped: wasSkipped
				})
			});

			if (response.ok) {
				console.log('Play record sent successfully');
				setHasRecordedPlay(true);
				setListeningTime(0);
			} else {
				console.error('Failed to send play record:', response.statusText);
			}
		} catch (error) {
			console.error('Error recording play:', error);
		}
	};

	// Toggle like
	const toggleLike = async () => {

		try {
			const method = isLiked ? 'DELETE' : 'POST';
			const response = await fetch(`${baseUrl}/api/like/${musicId}`, { method });

			if (!response.ok) {
				const error = await response.json();
				if (window.toastr) {
					window.toastr.error(`Failed to ${isLiked ? 'unlike' : 'like'}: ${error.error}`);
				}
				return;
			}

			setIsLiked(!isLiked);
			if (musicData) {
				setMusicData({
					...musicData,
					likeCount: isLiked ? musicData.music.likeCount - 1 : musicData.music.likeCount + 1
				});
			}

		} catch (error) {
			if (window.toastr) {
				window.toastr.error(`Error toggling like: ${error.message}`);
			}
		}
	};

	// Simple Play/Pause toggle
	const togglePlayPause = () => {
		if (wavesurferRef.current) {
			if (isPlaying) {
				wavesurferRef.current.pause();
			} else {
				wavesurferRef.current.play();
			}
		}
	};

	// Volume control
	const handleVolumeChange = (e) => {
		const newVolume = e.target.value / 100;
		setVolume(newVolume);
	};

	// Time formatting
	const formatTime = (time) => {
		const minutes = Math.floor(time / 60);
		const seconds = Math.floor(time % 60);
		return `${minutes}:${seconds.toString().padStart(2, '0')}`;
	};

	// Handle playlist selection
	const handlePlaylistSelected = (playlistId) => {
		setShowPlaylistModal(false);
		// You can add success notification here if needed
		if (window.toastr) {
			window.toastr.success('Added to playlist successfully!');
		}
	};

	return (
		<>
			<div className="row justify-content-center">
				<div className="col-md-12">
					<div className="card shadow-lg">
						<div className="card-header bg-primary">
						</div>

						<div className="card-body">

							{/* Music Info */}
							{musicData && (
								<div className="row mb-4">
									<div className="col-md-3">
										<img
											src={`${baseUrl}/stream/image/music/${musicId}`}
											alt={musicData.music.title}
											className="img-fluid rounded shadow-sm"
											style={{ aspectRatio: '1:1', objectFit: 'cover' }}
										/>
									</div>
									<div className="col-md-9">
										<h3 className="mb-2">{musicData.music.title}</h3>
										<pre><p className="text-muted mb-2">{musicData.music.description}</p></pre>
										<div className="d-flex flex-wrap gap-2 mb-3">
											<span className="badge bg-secondary">{musicData.music.genreName}</span>
											<span className="badge bg-info">{musicData.music.moodName}</span>
											{musicData.music.premiumContent && (
												<span className="badge bg-warning">Premium</span>
											)}
										</div>
										<div className="d-flex align-items-center gap-3 text-muted small">
											<span>
												<i className="bi bi-heart-fill text-danger me-1"></i>
												{musicData.music.likeCount} likes
											</span>
											<span>
												<i className="bi bi-play-fill text-primary me-1"></i>
												{musicData.music.totalPlayCount} plays
											</span>
											<span>
												<i className="bi bi-clock me-1"></i>
												{new Date(musicData.music.uploadDate).toLocaleDateString()}
											</span>
										</div>
									</div>
								</div>
							)}

							{/* Loading State */}
							{isLoading && (
								<div className="text-center mb-4">
									<div className="spinner-border text-primary" role="status">
										<span className="visually-hidden">Loading...</span>
									</div>
									<p className="mt-2 text-muted">Loading music...</p>
								</div>
							)}

							{/* Waveform Container */}
							<div className="mb-4">
								<div
									ref={containerRef}
								>
									{!musicData && !isLoading && (
										<span className="text-muted">Waveform will appear here</span>
									)}
								</div>
							</div>

							{/* Controls */}
							{musicData && (
								<>
									<div className="d-flex align-items-center justify-content-between mb-3">
										<div className="d-flex align-items-center gap-3">
											{/* Play/Pause Button */}
											<button
												className={`btn ${isPlaying ? 'btn-warning' : 'btn-primary'} btn-lg rounded-circle`}
												onClick={togglePlayPause}
												disabled={!musicData}
												style={{ width: '60px', height: '60px' }}
											>
												<i className={`bi ${isPlaying ? 'bi-pause-fill' : 'bi-play-fill'} fs-4`}></i>
											</button>

											{/* Time Display */}
											<div className="text-muted">
												<span>{formatTime(currentTime)}</span>
												<span className="mx-2">/</span>
												<span>{formatTime(duration)}</span>
											</div>
										</div>

										<div className="d-flex align-items-center gap-3">
											{/* Like Button */}
											<button
												className={`btn ${isLiked ? 'btn-danger' : 'btn-outline-danger'}`}
												onClick={toggleLike}
												disabled={!musicData}
											>
												<i className={`bi ${isLiked ? 'bi-heart-fill' : 'bi-heart'} me-2`}></i>
												Like
											</button>

											{/* Add to Playlist Button - Only shown for non-admin users */}
											{!isAdmin && (
												<button
													className="btn btn-outline-success"
													onClick={() => setShowPlaylistModal(true)}
													disabled={!musicData}
												>
													<i className="bi bi-plus-lg me-2"></i>
													Add to Playlist
												</button>
											)}

											{/* Volume Control */}
											<div className="d-flex align-items-center gap-2">
												<i className="bi bi-volume-down text-muted"></i>
												<input
													type="range"
													className="form-range"
													min="0"
													max="100"
													value={volume * 100}
													onChange={handleVolumeChange}
													style={{ width: '100px' }}
												/>
												<i className="bi bi-volume-up text-muted"></i>
											</div>
										</div>
									</div>

									{/* Progress Bar */}
									<div className="progress" style={{ height: '6px' }}>
										<div
											className="progress-bar bg-primary"
											ref={progressBarRef}
										></div>
									</div>
								</>
							)}
						</div>
					</div>
				</div>
			</div>
			{/* Artist Info Card */}
			{musicData && (
				<div className="row justify-content-center mt-3">
					<div className="col-md-12">
						<div
							className="card shadow-sm p-3 d-flex flex-row align-items-center gap-3"
							style={{ cursor: 'pointer' }}
							onClick={() => window.location.href = `${baseUrl}/channel/${musicData.music.artistId}`}
						>
							<img
								src={`${baseUrl}/stream/image/user/${musicData.music.artistId}`}
								alt={musicData.artistUsername}
								className="rounded-circle"
								style={{ width: '64px', height: '64px', objectFit: 'cover' }}
							/>
							<div>
								<h5 className="mb-0">{musicData.artistUsername}</h5>
								<p className="text-muted mb-0 small">View Artist Channel</p>
							</div>
						</div>
					</div>
				</div>
			)}

			{/* Playlist Choose Modal */}
			{showPlaylistModal && (
				<PlaylistChooseModal
					baseUrl={baseUrl}
					musicId={musicId}
					userId={userId}
					onClose={() => setShowPlaylistModal(false)}
					onPlaylistSelected={handlePlaylistSelected}
				/>
			)}
		</>
	);
};

export default MusicPlayer;