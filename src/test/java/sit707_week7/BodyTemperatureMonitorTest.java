package sit707_week7;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {

    @Test
    public void testStudentIdentity() {
        String studentId = "s223148345";
        org.junit.Assert.assertNotNull("Student ID is s223148345", studentId);
    }

    @Test
    public void testStudentName() {
        String studentName = "Bharat Bhatt";
        org.junit.Assert.assertNotNull("Student name is Bharat Bhatt", studentName);
    }

    @Test
    public void testReadTemperatureNegative() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();
        assertEquals(-1.0, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureZero() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();
        assertEquals(0.0, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureNormal() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        when(temperatureSensor.readTemperatureValue()).thenReturn(36.5);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();
        assertEquals(36.5, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureAbnormallyHigh() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        when(temperatureSensor.readTemperatureValue()).thenReturn(40.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();
        assertEquals(40.0, temperature, 0.01);
    }

    @Test
    public void testReportTemperatureReadingToCloud() {
        CloudService cloudService = mock(CloudService.class);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, null);

        TemperatureReading temperatureReading = new TemperatureReading();
        monitor.reportTemperatureReadingToCloud(temperatureReading);
        verify(cloudService).sendTemperatureToCloud(temperatureReading);
    }

    @Test
    public void testInquireBodyStatusNormalNotification() {
        CloudService cloudService = mock(CloudService.class);
        NotificationSender notificationSender = mock(NotificationSender.class);

        Customer fixedCustomer = new Customer();

        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("NORMAL");
        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);
        monitor.inquireBodyStatus();
        Mockito.verify(notificationSender, Mockito.times(1)).sendEmailNotification(Mockito.any(Customer.class), Mockito.anyString());    }

    @Test
    public void testInquireBodyStatusAbnormalNotification() {
        CloudService cloudService = mock(CloudService.class);
        NotificationSender NotificationSender = mock(NotificationSender.class);

        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("ABNORMAL");
        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, NotificationSender);
        monitor.inquireBodyStatus();
        Mockito.verify(NotificationSender, Mockito.times(1)).sendEmailNotification(Mockito.any(FamilyDoctor.class), Mockito.anyString());    }
}
