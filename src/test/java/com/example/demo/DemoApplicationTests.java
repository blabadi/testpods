package com.example.demo;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	public static final String CLIENT_CERT_DATA = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM4akNDQWRxZ0F3SUJBZ0lJZXZWZWt2VkVRT1V3RFFZ" +
		"SktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB4T1RBME1qSXdNekV4TVRWYUZ3MHlN" +
		"REEwTWpFd016RXhNVFZhTURReApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sa3dGd1lEVlFRREV4QnJkV0psY" +
		"201bGRHVnpMV0ZrCmJXbHVNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQTcxazlKMDE3Ry9FTXQ" +
		"wS1EKYUZEQ0VsSFlIdkdSNUdHUW1zbFFORit1enhZN056OXFRS1hoeHBHZVBtYlNnSDN0TDAyWHNCM0hWMkJFd1UrOAord1dq" +
		"SzdIYWlqV3U3czBZVlRkMFhCTkZnVmZPMU11ckgvcGlGL3FwSWY1T214bExlTEJma0FFam4zZ0JSOC9lCnZlMm4rbjBKaXN5dl" +
		"gyd1RLZkhNWUl5TDNRK2NEbEgxT1F1cldjZXhGNFpaYThoMk05TS84MGF2Z2dGQmZjclMKN2dqRmszcDM1WTJLY0wrMWtMVkZ" +
		"xbk54RWhuWHBtanhvVGdBZmI3YXhVZlpobmtwK0pXamFrQ1Q5NmM5VTZtdgoxbUxsRk00bEdGTjZIMlJpUnlhbUFkNURCRWhEZ" +
		"1pvczJUQXp3bmxDMzVmekFSQ291a2RTRGhsRm5rQkJsZERiClBGeEJrUUlEQVFBQm95Y3dKVEFPQmdOVkhROEJBZjhFQkFNQ0J" +
		"hQXdFd1lEVlIwbEJBd3dDZ1lJS3dZQkJRVUgKQXdJd0RRWUpLb1pJaHZjTkFRRUxCUUFEZ2dFQkFBaExYN1Zya1FRVXgvem1ac" +
		"Up1ai9HZ2NTTnpNVnhJK28wZAp4VmltOHBlMTVrVWJVQ3E3dGVZWkZwZmp2TDVqMm8rV2ozcFYyWHE2ejRtaEo4OTJ6VkpId29" +
		"qVVBBeDQya0FGClRJTGNiTDVpaGppQUtxcThVMFNZTmVibm1hUXJFaGQwQ2xMK1luWFFYZ3ZhUjQzTzk0Q2lUSG00alAxQUdDV" +
		"1MKb0hNNFFvWm1DQ2hxNG4vTWdtK0tCbVR0WDRFNEhZVnJOcVFFbVFCdExvQUZ2T2JYeVZad0VQYTVKem0rWFg4UAp2R0pyck5" +
		"CRFNKaCtyZ21zWk15ekRFMy9MSndyZUtrSXYyb1c0RlpVNHMxdDBISVhGcXQ2ZVFUTUtXbVVZa3h2CmFQSmFPeUdXNVpXVFdzQ" +
		"3RIM3JDcFZ6dXBhN2RRc29TeE1HTGR6a2o0dFpKdFp4c3lyUT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=";
	public static final String CLIENT_KEY_DATA = "LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFb3dJQkFBS0NBUUVBNzFrOUowMTdHL0VNdDB" +
		"LUWFGRENFbEhZSHZHUjVHR1Ftc2xRTkYrdXp4WTdOejlxClFLWGh4cEdlUG1iU2dIM3RMMDJYc0IzSFYyQkV3VSs4K3dXaks3SGFp" +
		"ald1N3MwWVZUZDBYQk5GZ1ZmTzFNdXIKSC9waUYvcXBJZjVPbXhsTGVMQmZrQUVqbjNnQlI4L2V2ZTJuK24wSmlzeXZYMndUS2ZIT" +
		"VlJeUwzUStjRGxIMQpPUXVyV2NleEY0WlphOGgyTTlNLzgwYXZnZ0ZCZmNyUzdnakZrM3AzNVkyS2NMKzFrTFZGcW5OeEVoblhw" +
		"bWp4Cm9UZ0FmYjdheFVmWmhua3ArSldqYWtDVDk2YzlVNm12MW1MbEZNNGxHRk42SDJSaVJ5YW1BZDVEQkVoRGdab3MKMlRBendu" +
		"bEMzNWZ6QVJDb3VrZFNEaGxGbmtCQmxkRGJQRnhCa1FJREFRQUJBb0lCQUNlM2ZERllaeFJVNUpKSQpabVFZK290RnI0STYvY1dT" +
		"OXdib1h2bkI0dVVVUUZGRG9hTUdvN2RxbElLd1Z6L2hKWW1TNjVmYTZTZ3pubFVoCm9TOWFXU0E0REJhaEg5MGFoVzRtbkpWbTBV" +
		"TE1TbEwvVk84aWdrS09ZcUIwYmFESkNzQUZMR1pkK2daTlNKRWQKQ2s0ajhKcmRRc2NZZmdWc2pxV0lMU3pjUloweDFydkZZU0Mz" +
		"OFFxdnFxMHc5VVJrLzFKUnVRK0x5YVc4dWVTSgpNa1hpcm5BOVJXYjBvTVBJcjcyckVoOEFEVFNPQW1uVjVmcXRLNmljcER4ajFBQ" +
		"zNDUE43ZUNpZmFqVEo1VGNhClUrNHh6cTRnUE54YmN0UmlKY0pQNzE3QXk5SnB5d1BDZW5zR0JHMlJodG10czEyaWFpZHp3YnhDbF" +
		"VraTZ3djgKcXJFWlhNRUNnWUVBL1lFRjVvc3k1dkV6ZjhyVjBVMCsyR3B5UXhNZGpxN1kxeU45SXJEV3ZLVC8ranNyZFZGWApCMjV" +
		"makllYkgyMGpkTi83cHFMRnRyMExhTXFKUjRyekF2MS9XQXNVVU1pN0pRaENKTloyQmhub0hrRVhMU2VoCkt0ZVpHYWVmdllkcFEy" +
		"TXNyV0EvUE1nWFNPMzZ4c3hpOW92MUlET3pFbmNsQlNKWTI1UjZ1WWtDZ1lFQThiU0oKTjV6OWhPZmVKYjZpQlNnY3VkYlJ2STZ4Q" +
		"lh0Mk0zbHI2eWlIT0NlMkxINVdzaFJKakRSTTdKSGhWSHA5cGhTTQpzekZvVXB2bWgvY2VJbU9ZTVNOVXUwZWtpSk01aXN0Vk5FL3" +
		"gxMGl6Y0dlWnVqalVDMkdUeFRXaW45OFhDaWhUCm1SaU9UQW5sbEhVQXB4UHdjQU15aldDTUtFaWxlOHpQaDE3bnJja0NnWUVBaH" +
		"FvSmRtU3ZqNXJGM3JialNxSWMKSmw2MWdBZ2hoWEJBWkZqSDJxdlNCcmduK3NkLzJIeHpITkYzRkhIYzU1N1VYUHUyOU5nMjYxSVZ" +
		"RMytEMld0bwptRWdOWTduTyt2TUc1eTh4NDl2QktwQ01pN1hNR0FLYlRPVG13WWdZQlZodmhJcWtheDFidW5BWVhuZDg1aW9nCj" +
		"BCT2REWlFwSmtBQWZHKzRnRHlwelBFQ2dZQUc1SndwTHR2NEIxakpnTWRFR0dIQlZ3UUNPWnVJeVgyVWVEa0EKcXRZNFhzR09RVW" +
		"dkcWdCeTlDYmhkRU9WMGl4MUtRN2VLV0dOUVk3d2Y3YW5TbW5UdE9zOHNBMWNLNzZ2VzU4aApiOG80MW9UdmhyVFduN3BFWE5NSE" +
		"E4R1FoNTh6bGZUVVgrUG0vVDFEMzVaV0xYWms2MkpzcjcwdmhPcmJQZnNKCkpFYTlLUUtCZ0ZzWmsxdTNSbDd0M1JRMHlFVnNsRm" +
		"pkZGk3NittcG1ka1ZUY1hwUjlWcHdrcnYxTDFtYkZpWTEKYXNFMm5nNFllenB2QXlGYzg0OXhiQzJzcFZnbU8rajBCNFFUUFVLQ" +
		"VVSU0xmdS9MYzIyRDg3R3ovN0RHYVpaawptTVhhNVFMSHJmN1ZXT2V0MEtxcGd4ZWhneGFXdTRSQUE2elFHeEYyTktrYnU3QkdsN" +
		"EliCi0tLS0tRU5EIFJTQSBQUklWQVRFIEtFWS0tLS0tCg==";
	public static final String CA_CERT_DATA = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUN5RENDQWJDZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0" +
		"ZBREFWTVJNd0VRWURWUVFERXdwcmRXSmwKY201bGRHVnpNQjRYRFRFNU1EUXlNakF6TVRFeE5Wb1hEVEk1TURReE9UQXpNVEV4TlZ" +
		"vd0ZURVRNQkVHQTFVRQpBeE1LYTNWaVpYSnVaWFJsY3pDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRGdnRVBBRENDQVFvQ2dnRUJBT" +
		"GtwCm53MGJOVEpDNnNvb2lYdVp3OXdnT1ZEajJ4ckViWkxXYVlvNTU4WmluRzRURG5NNUJPYjFWYU02bEJPTGsvSHgKME9qQUxSK0N6" +
		"NDh2d2lDL0lKWWEva3JQNkxqWWF5Qy82ZVdjRzlGanFIRllPU1FTSFhiN3ZpTHV3M0I0UmRnVwp0aXVRMnhFUWQ2VFptV0ptSWVZb0" +
		"8wdXdCVk9sUnNsZ1lrTWRvaTJUQm5KT2dHcUlMR3YyaVpjVFRGZ0hKczlhCktDTWRFeVltaEh4Z1NjT0JVUXRrdkVNVGZPeWZ0QVFy" +
		"Wlc2ZEs5azZnNDlpSXJaOElmd2dRZ0Rsd1NUbFN4VkIKYmNaRUFycFZvK3RYL2dIb3d4Q0E3RXlUb1hQRVFRTHZ3Sk1yaWg2djZkMj" +
		"VXOTh3dDZQUUhORjh0ZUt0WVN1QQpwYWhrM2Q5ekJxNWdNelB0cnlzQ0F3RUFBYU1qTUNFd0RnWURWUjBQQVFIL0JBUURBZ0trTUE4" +
		"R0ExVWRFd0VCCi93UUZNQU1CQWY4d0RRWUpLb1pJaHZjTkFRRUxCUUFEZ2dFQkFJdTB1ODlzNTBoelRmV0VtVkNTM3g5ZnR1K0wKd0" +
		"ErUjliSU81dzRuMGsvUVduSktPME9VYlEvYnRpOFMrT3NDcHhSOXBlRkFYWHR1MC9FOEwvUWZDcWdzaDl2ZwpxbEFTdXJxOTZrQnV2" +
		"U2dYb25scmhEWWdFeGV1UmttVjQyUzYyUnV1L25kQ1BDNnJpajRyeEozQ1VOSXJHK2xFCkFsZVE2dFBWd3BjaWppcXlwTklzMm13cV" +
		"lIVW43Q0dCNFhCZkxhdVREQVlOTHdaeWxCTzRSOFlyWm5PT1JOZkwKVWpiNUhCMGx2SU92aUpZd2VmclhwTHBJK3c1QkRlQW4rTHZl" +
		"Um1TeStOb244VUVnYWVHSkV1YmVCOGMxOUhEMQpPSzljKytWMnBvVGs2dlBNK1JVMGY5eXE5YjFYazd0VDRrOTB2clJPSXpsc0tEWW" +
		"5zNkcvc0ozeUhKWT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=";
	static KubernetesClient client;

	static String res =
		"apiVersion: v1\n" +
		"kind: Pod\n" +
		"metadata:\n" +
		"  name: my-nginx\n" +
		"  labels:\n" +
		"    app: api\n" +
		"spec:\n" +
		"  containers:\n" +
		"  - name: nginx-cont\n" +
		"    image: nginx\n" +
		"    ports:\n" +
		"    - containerPort: 80\n" +
		"---\n" +
		"apiVersion: v1\n" +
		"kind: Service\n" +
		"metadata:\n" +
		"  name: api-svc\n" +
		"spec:\n" +
		"  type: NodePort\n" +
		"  selector:\n" +
		"    app: api\n" +
		"  ports:\n" +
		"    - protocol: TCP\n" +
		"      name: tcp\n" +
		"      port: 3311\n" +
		"      targetPort: 80\n";

	static int servicePort;
	static String ip;

	@BeforeClass
	public static void beforeClass() throws InterruptedException {
		Config config = new ConfigBuilder().withMasterUrl("https://localhost:45205")
			.withUsername("kubernetes-admin")
			.withNamespace("default")
			.withCaCertData(CA_CERT_DATA)
			.withClientKeyData(CLIENT_KEY_DATA)
			.withClientCertData(CLIENT_CERT_DATA)
			.build();

		client = new DefaultKubernetesClient(config);
		InputStream stream = new ByteArrayInputStream(res.getBytes(StandardCharsets.UTF_8));
		var result = client.load(stream)
			.delete();
		System.out.println(result);
		Thread.sleep(3000);

		InputStream stream2 = new ByteArrayInputStream(res.getBytes(StandardCharsets.UTF_8));
		var result2 = client.load(stream2)
			.createOrReplace();
		Thread.sleep(3000);
		System.out.println(result2);
		servicePort =  ((Service) result2.get(1)).getSpec().getPorts().get(0).getNodePort();
		ip = client.pods().list().getItems().get(0).getStatus().getHostIP();
	}

	@AfterClass
	public static void afterAll() {
		InputStream stream = new ByteArrayInputStream(res.getBytes(StandardCharsets.UTF_8));
		var result = client.load(stream).deletingExisting();
		System.out.println(result);
	}

	@Test
	public void contextLoads() {
		System.out.println(WebClient.create().get().uri("http://" + ip + ":" + servicePort).retrieve().bodyToMono(String.class).block());
	}

}
