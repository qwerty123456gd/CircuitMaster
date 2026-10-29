document.addEventListener('DOMContentLoaded', () => {

    const screens = {
        welcome: document.getElementById('screen-welcome'),
        main: document.getElementById('screen-main'),
        course: document.getElementById('screen-course'),
        textbooks: document.getElementById('screen-textbooks'),
        tests: document.getElementById('screen-tests'),
        calculator: document.getElementById('screen-calculator'),
        charts: document.getElementById('screen-charts')
    };

    const screenTitles = {
        main: 'Главная',
        course: 'Курс',
        textbooks: 'Учебники',
        tests: 'Задачки и тесты',
        calculator: 'Калькулятор',
        charts: 'Графики и таблицы'
    };

    const mobileHeaderTitle = document.getElementById('mobile-header-title');

    function switchScreen(screenName) {
        Object.values(screens).forEach(s => s && s.classList.add('hidden'));
        if (screens[screenName]) {
            screens[screenName].classList.remove('hidden');
        }
        if (mobileHeaderTitle && screenTitles[screenName]) {
            mobileHeaderTitle.textContent = screenTitles[screenName];
        }
        closeDrawer();
    }

    const btnStart = document.getElementById('btn-start');
    if (btnStart) btnStart.addEventListener('click', () => switchScreen('main'));

    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', (e) => {
            document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
            e.currentTarget.classList.add('active');
            switchScreen(e.currentTarget.dataset.screen);
        });
    });

    // Мобильное меню
    const sidebar = document.getElementById('sidebar');
    const drawerOverlay = document.getElementById('drawer-overlay');
    const btnOpenDrawer = document.getElementById('btn-open-drawer');

    function openDrawer() {
        if (sidebar && drawerOverlay) {
            sidebar.classList.remove('-translate-x-full');
            drawerOverlay.classList.remove('hidden');
        }
    }
    function closeDrawer() {
        if (sidebar && drawerOverlay) {
            sidebar.classList.add('-translate-x-full');
            drawerOverlay.classList.add('hidden');
        }
    }

    if (btnOpenDrawer) btnOpenDrawer.addEventListener('click', openDrawer);
    if (drawerOverlay) drawerOverlay.addEventListener('click', closeDrawer);

    // Профиль
    const profileModal = document.getElementById('profile-modal');
    function openProfile() { if (profileModal) { profileModal.classList.remove('hidden'); profileModal.classList.add('flex'); } }
    function closeProfile() { if (profileModal) { profileModal.classList.add('hidden'); profileModal.classList.remove('flex'); } }

    document.getElementById('btn-profile-mobile')?.addEventListener('click', openProfile);
    document.getElementById('btn-profile-desktop')?.addEventListener('click', openProfile);
    document.getElementById('btn-close-profile')?.addEventListener('click', closeProfile);
    document.getElementById('btn-close-profile-bottom')?.addEventListener('click', closeProfile);

    // Аккордеоны Частей курса
    document.querySelectorAll('.accordion-header').forEach(header => {
        header.addEventListener('click', () => {
            header.classList.toggle('active');
        });
    });

    // Вложенные аккордеоны Параграфов
    document.querySelectorAll('.nested-header').forEach(header => {
        header.addEventListener('click', (e) => {
            e.stopPropagation();
            header.classList.toggle('active');
        });
    });

    // Открываем первую часть по умолчанию
    document.querySelector('.accordion-header')?.classList.add('active');
});

// --- КАЛЬКУЛЯТОР ---
let calcInput = '0';
const calcDisplay = document.getElementById('calc-display');

function updateCalcDisplay() { if (calcDisplay) calcDisplay.textContent = calcInput; }

function calcAppend(val) { 
    if (calcInput === '0' && val !== '.' && val !== '**') {
        calcInput = val;
    } else {
        calcInput += val;
    }
    updateCalcDisplay(); 
}

function calcClear() { calcInput = '0'; updateCalcDisplay(); }

function calcBackspace() { 
    calcInput = calcInput.slice(0, -1) || '0'; 
    updateCalcDisplay(); 
}

function calcSolve() {
    try {
        let expr = calcInput.replace(/%/g, '/100');
        let result = eval(expr);
        calcInput = String(Math.round(result * 1000000) / 1000000);
    } catch (e) {
        calcInput = 'Ошибка';
    }
    updateCalcDisplay();
}

function calcSquare() {
    try {
        let val = eval(calcInput.replace(/%/g, '/100'));
        calcInput = String(Math.pow(val, 2));
    } catch (e) { calcInput = 'Ошибка'; }
    updateCalcDisplay();
}

function calcSqrt() {
    try {
        let val = eval(calcInput.replace(/%/g, '/100'));
        calcInput = String(Math.sqrt(val));
    } catch (e) { calcInput = 'Ошибка'; }
    updateCalcDisplay();
}

function calcFunction(func) {
    try {
        let val = eval(calcInput.replace(/%/g, '/100'));
        let res = 0;
        switch (func) {
            case 'sin': res = Math.sin(val * Math.PI / 180); break;
            case 'cos': res = Math.cos(val * Math.PI / 180); break;
            case 'tan': res = Math.tan(val * Math.PI / 180); break;
            case 'exp': res = Math.exp(val); break;
            case 'ln': res = Math.log(val); break;
            case 'abs': res = Math.abs(val); break;
        }
        calcInput = String(Math.round(res * 1000000) / 1000000);
    } catch (e) {
        calcInput = 'Ошибка';
    }
    updateCalcDisplay();
}

// Электротехнические расчеты
function calculateOhm() {
    const uInput = document.getElementById('ohm-u');
    const rInput = document.getElementById('ohm-r');
    const iInput = document.getElementById('ohm-i');
    const resultText = document.getElementById('ohm-result');

    let u = parseFloat(uInput.value);
    let r = parseFloat(rInput.value);
    let i = parseFloat(iInput.value);

    if (!isNaN(u) && !isNaN(r)) {
        let resI = u / r;
        iInput.value = resI.toFixed(3);
        resultText.textContent = `Расчитано: Сила тока I = ${resI.toFixed(3)} А`;
    } else if (!isNaN(i) && !isNaN(r)) {
        let resU = i * r;
        uInput.value = resU.toFixed(3);
        resultText.textContent = `Расчитано: Напряжение U = ${resU.toFixed(3)} В`;
    } else if (!isNaN(u) && !isNaN(i)) {
        let resR = u / i;
        rInput.value = resR.toFixed(3);
        resultText.textContent = `Расчитано: Сопротивление R = ${resR.toFixed(3)} Ом`;
    } else {
        resultText.textContent = 'Заполните хотя бы 2 поля из 3!';
    }
}

function calcResistors(type) {
    const r1 = parseFloat(document.getElementById('res-r1').value);
    const r2 = parseFloat(document.getElementById('res-r2').value);
    const resResult = document.getElementById('res-result');

    if (isNaN(r1) || isNaN(r2)) {
        resResult.textContent = 'Введите значения R1 и R2';
        return;
    }

    if (type === 'series') {
        let req = r1 + r2;
        resResult.textContent = `${req.toFixed(2)} Ом (Последовательно)`;
    } else if (type === 'parallel') {
        if (r1 + r2 === 0) {
            resResult.textContent = 'Деление на ноль';
            return;
        }
        let req = (r1 * r2) / (r1 + r2);
        resResult.textContent = `${req.toFixed(2)} Ом (Параллельно)`;
    }
}