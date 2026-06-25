package com.example.rent.core.mock

import com.example.rent.core.utils.DisplayFormatters
import com.example.rent.presentation.model.BillStatus
import com.example.rent.presentation.model.BillUiModel
import com.example.rent.presentation.model.HomeOverviewUiModel
import com.example.rent.presentation.model.HomeQuickActionUiModel
import com.example.rent.presentation.model.MeterReadingUiModel
import com.example.rent.presentation.model.NotificationType
import com.example.rent.presentation.model.NotificationUiModel
import com.example.rent.presentation.model.PaymentUiModel
import com.example.rent.presentation.model.ProfileSummaryUiModel
import com.example.rent.presentation.model.ProfileUiModel
import com.example.rent.presentation.model.RecentActivityUiModel
import com.example.rent.presentation.model.RoomStatus
import com.example.rent.presentation.model.RoomUiModel
import com.example.rent.presentation.model.TenantStatus
import com.example.rent.presentation.model.TenantUiModel

object SmartRentMockData {
    val homeOverview = listOf(
        HomeOverviewUiModel("Tong so phong", "24", "3 day dang quan ly"),
        HomeOverviewUiModel("Dang thue", "19", "79% lap day"),
        HomeOverviewUiModel("Phong trong", "5", "Can cap nhat gia"),
        HomeOverviewUiModel("Nguoi thue", "32", "4 hop dong moi"),
        HomeOverviewUiModel("Hoa don cho thu", "8", "2 hoa don qua han"),
        HomeOverviewUiModel("Thu thang nay", DisplayFormatters.currencyDisplay("68,5 trieu d"), "+12% so voi thang truoc"),
    )

    val homeQuickActions = listOf(
        HomeQuickActionUiModel("add_room", "Them phong"),
        HomeQuickActionUiModel("add_tenant", "Them nguoi thue"),
        HomeQuickActionUiModel("create_bill", "Tao hoa don"),
        HomeQuickActionUiModel("record_meter", "Ghi dien/nuoc", enabled = false),
    )

    val recentActivities = listOf(
        RecentActivityUiModel("Nguoi thue moi", "Minh Anh da duoc them vao phong A203", "Vua xong"),
        RecentActivityUiModel("Hoa don da tao", "Hoa don thang 06/2026 cho day B", "Hom nay"),
        RecentActivityUiModel("Phong vua cap nhat", "Phong C105 chuyen sang trang thai trong", "Hom qua"),
    )

    val rooms = listOf(
        RoomUiModel(
            id = "room_101",
            name = "Phong 101",
            meta = "Tang 1 - ${DisplayFormatters.roomCapacity(2, 3)}",
            priceDisplay = DisplayFormatters.currencyDisplay("3.500.000 d/thang"),
            note = "Hoa don thang nay cho thu",
            status = RoomStatus.OCCUPIED,
        ),
        RoomUiModel(
            id = "room_203",
            name = "Phong 203",
            meta = "Tang 2 - Con trong",
            priceDisplay = DisplayFormatters.currencyDisplay("3.200.000 d/thang"),
            note = "San sang nhan nguoi thue moi",
            status = RoomStatus.AVAILABLE,
        ),
        RoomUiModel(
            id = "room_305",
            name = "Phong 305",
            meta = "Tang 3 - Bao tri",
            priceDisplay = DisplayFormatters.currencyDisplay("3.800.000 d/thang"),
            note = "Dang sua may nuoc nong",
            status = RoomStatus.MAINTENANCE,
        ),
    )

    val tenants = listOf(
        TenantUiModel(
            id = "tenant_minh_anh",
            name = "Nguyen Minh Anh",
            phoneDisplay = "0912 *** 678",
            roomLabel = "Phong 101 - Thue tu 01/03/2026",
            note = "Thanh toan on dinh - hoa don thang nay cho thu",
            status = TenantStatus.ACTIVE,
        ),
        TenantUiModel(
            id = "tenant_lan_huong",
            name = "Tran Lan Huong",
            phoneDisplay = "0988 *** 233",
            roomLabel = "Phong 203 - Hop dong con 18 ngay",
            note = "Can nhac gia han hop dong trong thang nay",
            status = TenantStatus.ENDING_SOON,
        ),
        TenantUiModel(
            id = "tenant_quoc_bao",
            name = "Le Quoc Bao",
            phoneDisplay = "0903 *** 889",
            roomLabel = "Phong 305 - Thue tu 15/11/2025",
            note = "Con no 1.250.000 d tu hoa don gan nhat",
            status = TenantStatus.DEBT,
        ),
    )

    val bills = listOf(
        BillUiModel(
            id = "bill_0626_101",
            title = "HD-0626-101",
            meta = "Phong 101 - Nguyen Minh Anh - Thang 06/2026",
            amountDisplay = DisplayFormatters.currencyDisplay("3.850.000 d"),
            dueLabel = "Han thanh toan 25/06/2026",
            status = BillStatus.PENDING,
        ),
        BillUiModel(
            id = "bill_0626_203",
            title = "HD-0626-203",
            meta = "Phong 203 - Tran Lan Huong - Da thu ngay 18/06/2026",
            amountDisplay = DisplayFormatters.currencyDisplay("3.420.000 d"),
            dueLabel = "Da thanh toan",
            status = BillStatus.PAID,
        ),
        BillUiModel(
            id = "bill_0526_305",
            title = "HD-0526-305",
            meta = "Phong 305 - Le Quoc Bao - Qua han 7 ngay",
            amountDisplay = DisplayFormatters.currencyDisplay("1.250.000 d con no"),
            dueLabel = "Qua han",
            status = BillStatus.OVERDUE,
        ),
    )

    val meterReading = MeterReadingUiModel(
        roomName = "Phong 101",
        periodLabel = "Thang 06/2026",
        electricStart = "182",
        waterStart = "9",
    )

    val payment = PaymentUiModel(
        billLabel = "HD-0626-101 - Phong 101",
        totalDisplay = DisplayFormatters.currencyDisplay("Tong tien: 3.850.000 d - Da thanh toan: 0 d"),
        remainingDisplay = DisplayFormatters.currencyDisplay("Con lai: 3.850.000 d"),
    )

    val profile = ProfileUiModel(
        displayName = "Nguyen Van Thanh",
        roleLabel = "Chu nha",
        contactDisplay = "thanh.owner@example.com - 0909 *** 777",
        summaries = listOf(
            ProfileSummaryUiModel("Day dang quan ly", "3"),
            ProfileSummaryUiModel("Tong phong", "24"),
            ProfileSummaryUiModel("Nguoi thue", "32"),
            ProfileSummaryUiModel("Cho thu", "8"),
        ),
    )

    val notifications = listOf(
        NotificationUiModel(
            id = "notification_bill_due",
            title = "Hoa don phong 101 sap den han",
            body = "HD-0626-101 con 3.850.000 d, han thanh toan 25/06/2026.",
            timeLabel = "09:20 hom nay",
            type = NotificationType.BILL,
            isUnread = true,
        ),
        NotificationUiModel(
            id = "notification_room_available",
            title = "Phong 203 da chuyen sang trong",
            body = "Cap nhat trang thai phong de chuan bi nhan khach moi.",
            timeLabel = "08:05 hom nay",
            type = NotificationType.ROOM,
            isUnread = false,
        ),
        NotificationUiModel(
            id = "notification_contract",
            title = "Hop dong Lan Huong con 18 ngay",
            body = "Can nhac gia han hop dong trong thang nay.",
            timeLabel = "Hom qua",
            type = NotificationType.TENANT,
            isUnread = false,
        ),
        NotificationUiModel(
            id = "notification_meter_missing",
            title = "Chua ghi chi so dien nuoc day B",
            body = "2 phong chua co chi so cuoi ky thang 06/2026.",
            timeLabel = "20/06/2026",
            type = NotificationType.METER,
            isUnread = false,
        ),
    )
}
