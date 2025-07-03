// Admin Statistics Dashboard JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Initialize all charts
    initializeDashboard();
});

// Global chart instances
let dailySalesChart, topProductsChart, monthlySalesChart, categoryStatsChart, orderStatusChart;

// Chart.js default configuration
Chart.defaults.font.family = "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif";
Chart.defaults.color = '#666';

// Initialize the dashboard
async function initializeDashboard() {
    try {
        console.log('Initializing dashboard...');
        
        // Load and render all charts
        await Promise.all([
            loadDailySalesChart(),
            loadTopProductsChart(),
            loadMonthlySalesChart(),
            loadCategoryStatsChart(),
            loadOrderStatusChart()
        ]);
        
        // Load summary statistics
        await loadSummaryStats();
          console.log('Dashboard initialized successfully');
    } catch (error) {
        console.error('Error initializing dashboard:', error);
    }
}

// Show error state for a specific chart
function showChartError(chartId, message) {
    const chartElement = document.getElementById(chartId);
    if (chartElement && chartElement.parentElement) {
        chartElement.parentElement.innerHTML = `<div class="chart-error">${message}</div>`;
    }
}

// Load Daily Sales Chart
async function loadDailySalesChart() {
    try {
        const response = await fetch(`${contextPath}/front?key=admin&methodName=getDailySalesData`);
        const data = await response.json();
        
        const ctx = document.getElementById('dailySalesChart').getContext('2d');
          dailySalesChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: data.labels || [],
                datasets: [{
                    label: '일별 매출',
                    data: data.totalAmounts || [],
                    borderColor: '#667eea',
                    backgroundColor: 'rgba(102, 126, 234, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4,
                    pointBackgroundColor: '#667eea',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: '최근 30일 일별 매출 현황',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return '₩' + value.toLocaleString();
                            }
                        }
                    },
                    x: {
                        grid: {
                            display: false
                        }
                    }
                },
                elements: {
                    point: {
                        hoverRadius: 8
                    }
                }
            }
        });    } catch (error) {
        console.error('Error loading daily sales chart:', error);
        showChartError('dailySalesChart', '일별 매출 차트를 불러올 수 없습니다.');
    }
}

// Load Top Products Chart
async function loadTopProductsChart() {
    try {
        const response = await fetch(`${contextPath}/front?key=admin&methodName=getTopProductsData`);
        const data = await response.json();
        
        const ctx = document.getElementById('topProductsChart').getContext('2d');
        
        const colors = [
            '#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe',
            '#43e97b', '#38f9d7', '#ffecd2', '#fcb69f', '#a8edea'
        ];
          topProductsChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.labels || [],
                datasets: [{
                    label: '판매량',
                    data: data.quantities || [],
                    backgroundColor: colors.slice(0, (data.labels || []).length),
                    borderColor: colors.slice(0, (data.labels || []).length),
                    borderWidth: 1,
                    borderRadius: 6,
                    borderSkipped: false
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: '인기 상품 TOP 10',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    },
                    x: {
                        ticks: {
                            maxRotation: 45
                        }
                    }
                }
            }
        });    } catch (error) {
        console.error('Error loading top products chart:', error);
        showChartError('topProductsChart', '인기 상품 차트를 불러올 수 없습니다.');
    }
}

// Load Monthly Sales Chart
async function loadMonthlySalesChart() {
    try {
        const response = await fetch(`${contextPath}/front?key=admin&methodName=getMonthlySalesData`);
        const data = await response.json();
        
        const ctx = document.getElementById('monthlySalesChart').getContext('2d');
          monthlySalesChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.labels || [],
                datasets: [{
                    label: '월별 매출',
                    data: data.totalAmounts || [],
                    backgroundColor: 'rgba(118, 75, 162, 0.8)',
                    borderColor: '#764ba2',
                    borderWidth: 1,
                    borderRadius: 8,
                    borderSkipped: false
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: '월별 매출 현황',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return '₩' + value.toLocaleString();
                            }
                        }
                    }
                }
            }
        });    } catch (error) {
        console.error('Error loading monthly sales chart:', error);
        showChartError('monthlySalesChart', '월별 매출 차트를 불러올 수 없습니다.');
    }
}

// Load Category Stats Chart
async function loadCategoryStatsChart() {
    try {
        const response = await fetch(`${contextPath}/front?key=admin&methodName=getCategoryStatsData`);
        const data = await response.json();
        
        const ctx = document.getElementById('categoryStatsChart').getContext('2d');
        
        const colors = [
            '#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe',
            '#43e97b', '#38f9d7', '#ffecd2', '#fcb69f', '#a8edea'
        ];
          categoryStatsChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: data.labels || [],
                datasets: [{
                    data: data.totalSales || [],
                    backgroundColor: colors.slice(0, (data.labels || []).length),
                    borderColor: '#fff',
                    borderWidth: 3,
                    hoverBorderWidth: 5
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: '카테고리별 매출 분포',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.label || '';
                                const value = context.parsed;
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = ((value / total) * 100).toFixed(1);
                                return `${label}: ₩${value.toLocaleString()} (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });    } catch (error) {
        console.error('Error loading category stats chart:', error);
        showChartError('categoryStatsChart', '카테고리 통계 차트를 불러올 수 없습니다.');
    }
}

// Load Order Status Chart
async function loadOrderStatusChart() {
    try {
        const response = await fetch(`${contextPath}/front?key=admin&methodName=getOrderStatusData`);
        const data = await response.json();
        
        const ctx = document.getElementById('orderStatusChart').getContext('2d');
          const statusColors = {
            '주문접수': '#28a745',
            '배송준비중': '#ffc107',
            '배송중': '#17a2b8',
            '배송완료': '#6f42c1',
            '주문취소': '#dc3545',
            '환불': '#fd7e14'
        };
        
        orderStatusChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: data.labels || [],
                datasets: [{
                    data: data.data || [],
                    backgroundColor: (data.labels || []).map(label => statusColors[label] || '#6c757d'),
                    borderColor: '#fff',
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: '주문 상태별 분포',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.label || '';
                                const value = context.parsed;
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = ((value / total) * 100).toFixed(1);
                                return `${label}: ${value}건 (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });    } catch (error) {
        console.error('Error loading order status chart:', error);
        showChartError('orderStatusChart', '주문 상태 차트를 불러올 수 없습니다.');
    }
}

// Load Summary Statistics
async function loadSummaryStats() {
    try {
        // Load individual statistics
        const [totalRevenue, totalOrders, totalUsers] = await Promise.all([
            fetch(`${contextPath}/front?key=admin&methodName=getTotalRevenue`).then(r => r.text()),
            fetch(`${contextPath}/front?key=admin&methodName=getTotalOrders`).then(r => r.text()),
            fetch(`${contextPath}/front?key=admin&methodName=getTotalUsers`).then(r => r.text())
        ]);

        // Update summary cards
        document.getElementById('totalRevenue').textContent = `₩${parseInt(totalRevenue || 0).toLocaleString()}`;
        
        const totalOrdersElement = document.querySelector('.stat-card:nth-child(2) .stat-number');
        if (totalOrdersElement) {
            totalOrdersElement.textContent = parseInt(totalOrders || 0).toLocaleString();
        }
        
        const totalUsersElement = document.querySelector('.stat-card:nth-child(4) .stat-number');
        if (totalUsersElement) {
            totalUsersElement.textContent = parseInt(totalUsers || 0).toLocaleString();
        }

    } catch (error) {
        console.error('Error loading summary stats:', error);
        // Keep default values on error
    }
}

// Update summary card
function updateSummaryCard(cardId, value, change, changeType) {
    const card = document.getElementById(cardId);
    if (card) {
        const valueElement = card.querySelector('.stat-value');
        const changeElement = card.querySelector('.stat-change');
        
        if (valueElement) valueElement.textContent = value;
        if (changeElement) {
            changeElement.textContent = change;
            changeElement.className = `stat-change ${changeType}`;
        }
    }
}

// Refresh all charts
function refreshDashboard() {
    // Destroy existing charts
    if (dailySalesChart) dailySalesChart.destroy();
    if (topProductsChart) topProductsChart.destroy();
    if (monthlySalesChart) monthlySalesChart.destroy();
    if (categoryStatsChart) categoryStatsChart.destroy();
    if (orderStatusChart) orderStatusChart.destroy();
    
    // Reinitialize
    initializeDashboard();
}

// Export chart as image
function exportChart(chartInstance, filename) {
    const link = document.createElement('a');
    link.download = filename + '.png';
    link.href = chartInstance.toBase64Image();
    link.click();
}

// Print dashboard
function printDashboard() {
    window.print();
}

// Utility function to format currency
function formatCurrency(amount) {
    return '₩' + amount.toLocaleString('ko-KR');
}

// Utility function to format date
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR');
}

// Add event listeners for any interactive elements
document.addEventListener('DOMContentLoaded', function() {
    // Add refresh button functionality if it exists
    const refreshBtn = document.getElementById('refresh-dashboard');
    if (refreshBtn) {
        refreshBtn.addEventListener('click', refreshDashboard);
    }
    
    // Add print button functionality if it exists
    const printBtn = document.getElementById('print-dashboard');
    if (printBtn) {
        printBtn.addEventListener('click', printDashboard);
    }
});

// Auto-refresh dashboard every 5 minutes
setInterval(refreshDashboard, 5 * 60 * 1000);
