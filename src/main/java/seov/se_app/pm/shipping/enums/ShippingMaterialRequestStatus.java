package seov.se_app.pm.shipping.enums;

public enum ShippingMaterialRequestStatus {
    SUBMITTED,   // Chờ duyệt
    APPROVED,    // Đã duyệt
    REJECTED,    // Từ chối
    COMPLETED,   // Đã xuất kho
    CANCELLED    // Đã hủy
}
