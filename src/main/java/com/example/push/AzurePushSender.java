
import com.azure.messaging.notificationhubs.*;
import com.azure.messaging.notificationhubs.models.*;
import java.util.HashMap;

public class AzurePushSenderNewSDK {
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("使用方法：java AzurePushSenderNewSDK <hubName> <connectionString> <deviceToken> <platform> <jsonPayload>");
            System.out.println("例：java AzurePushSenderNewSDK pushhub \"Endpoint=...\" xxxxxx android '{\"data\":{\"message\":\"hello\"}}'");
            return;
        }

        String hubName = args[0];
        String connectionString = args[1];
        String deviceToken = args[2];
        String platform = args[3].toLowerCase(); // ios 或 android
        String jsonPayload = args[4];

        try {
            NotificationHubClient client = new NotificationHubClientBuilder()
                .connectionString(connectionString)
                .hubName(hubName)
                .buildClient();

            Notification notification;
            if (platform.equals("android")) {
                notification = Notification.createFcmNotification(jsonPayload);
            } else if (platform.equals("ios")) {
                notification = Notification.createAppleNotification(jsonPayload);
            } else {
                System.out.println("❌ エラー：platformは android または ios にしてください。");
                return;
            }

            NotificationOptions options = new NotificationOptions()
                .setDeviceHandle(deviceToken);

            NotificationSendResult result = client.sendNotification(notification, options);

            // 输出发送结果
            if (result != null) {
                System.out.println("✅ PUSH SUCCESS:");
                System.out.println("  - Tracking ID: " + result.getTrackingId());
                System.out.println("  - Correlation ID: " + result.getCorrelationId());
                System.out.println("  - Notification ID: " + result.getNotificationId());
                System.out.println("  - State: " + result.getState());
            } else {
                System.out.println("❌ Error: NotificationSendResult is null.");
            }

        } catch (Exception e) {
            System.err.println("❌ 異常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
