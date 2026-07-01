const overlay = document.getElementById("image-viewer-overlay");
const overlayCloseButton = document.getElementById("overlay-close-button");
const imageViewer = document.getElementById("image-viewer");

const images = document.getElementsByClassName("gallery-image");

Array.from(images).forEach( img => {
        img.addEventListener("click", () => openImagePopup(img));
    }
)

overlayCloseButton.addEventListener("click", closeImagePopup);

document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
        closeImagePopup();
    }
})

function openImagePopup(img){
    overlay.classList.remove("display-none");
    imageViewer.src = img.src;
    imageViewer.alt = img.src.toString()
        .split("/")
        .at(-1)
        .split(".")
        .at(0)
        .replaceAll("-", " ")
        .replace(/\b\w/g, c => c.toUpperCase()); // \b finds the word boundary, \w gets the first character, g for global, to find multiple instances of said regex in one string,
}

function closeImagePopup(){
    overlay.classList.add("display-none");
}
