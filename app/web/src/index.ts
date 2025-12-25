const img = document.getElementById('frame') as HTMLImageElement;
const info = document.getElementById('info') as HTMLElement;

// paste base64 image data here or load a sample file
const sampleBase64 = "data:image/png;base64,...."; // Put actual base64 saved from Android
img.src = sampleBase64;
info.innerText = `FPS: 0 | Resolution: 640x480`;
