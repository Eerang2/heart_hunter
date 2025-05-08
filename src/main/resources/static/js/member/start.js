// Scroll Animation
const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('visible');
        }
    });
});

document.querySelectorAll('.intro-block').forEach(block => {
    observer.observe(block);
});

function openLoginModal() {
    document.getElementById('loginModal').style.display = 'flex';
}

function closeLoginModal(event) {
    if (event.target.id === 'loginModal' || event.target.className === 'close-modal') {
        document.getElementById('loginModal').style.display = 'none';
    }
}