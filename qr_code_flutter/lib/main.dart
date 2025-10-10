import 'package:flutter/material.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Wi-Fi Easy Connect QR Code',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const QRCodeScreen(),
    );
  }
}

class QRCodeScreen extends StatefulWidget {
  const QRCodeScreen({super.key});

  @override
  State<QRCodeScreen> createState() => _QRCodeScreenState();
}

class _QRCodeScreenState extends State<QRCodeScreen> {
  String? dppUri;
  bool isLoading = true;
  String? errorMessage;

  @override
  void initState() {
    super.initState();
    _loadDppUri();
  }

  Future<void> _loadDppUri() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final uri = prefs.getString('Dpp for QR code Generator');
      
      setState(() {
        dppUri = uri;
        isLoading = false;
        if (uri == null || uri.isEmpty) {
          errorMessage = 'Unable to get QRCode from DPP URI';
        }
      });
    } catch (e) {
      setState(() {
        isLoading = false;
        errorMessage = 'Error loading DPP URI: $e';
      });
    }
  }

  Future<void> _setDemoDppUri() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      const demoDppUri = 'DPP:C:81/1;M:00:c0:ca:97:64:ca;K:MDkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDIgADURzxmttZoIRIPWGoQMV00XHWCAQIhXruVWOz0NjlkIA=;;';
      await prefs.setString('Dpp for QR code Generator', demoDppUri);
      
      setState(() {
        isLoading = true;
        errorMessage = null;
      });
      _loadDppUri();
    } catch (e) {
      setState(() {
        errorMessage = 'Error setting demo URI: $e';
      });
    }
  }

  Future<void> _clearDppUri() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.remove('Dpp for QR code Generator');
      
      setState(() {
        isLoading = true;
        errorMessage = null;
      });
      _loadDppUri();
    } catch (e) {
      setState(() {
        errorMessage = 'Error clearing URI: $e';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text('AP QR Code'),
      ),
      body: Center(
        child: isLoading
            ? const CircularProgressIndicator()
            : _buildContent(),
      ),
    );
  }

  Widget _buildContent() {
    if (errorMessage != null) {
      return Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.error_outline,
              size: 64,
              color: Colors.red,
            ),
            const SizedBox(height: 16),
            Text(
              errorMessage!,
              style: const TextStyle(fontSize: 16),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 24),
            ElevatedButton.icon(
              onPressed: () {
                setState(() {
                  isLoading = true;
                  errorMessage = null;
                });
                _loadDppUri();
              },
              icon: const Icon(Icons.refresh),
              label: const Text('Retry'),
            ),
            const SizedBox(height: 12),
            ElevatedButton.icon(
              onPressed: _setDemoDppUri,
              icon: const Icon(Icons.add),
              label: const Text('Load Demo DPP URI'),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.green,
                foregroundColor: Colors.white,
              ),
            ),
          ],
        ),
      );
    }

    if (dppUri != null && dppUri!.isNotEmpty) {
      return Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              'Scan this QR code to connect',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 32),
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.1),
                    blurRadius: 10,
                    offset: const Offset(0, 4),
                  ),
                ],
              ),
              child: QrImageView(
                data: dppUri!,
                version: QrVersions.auto,
                size: 300.0,
                backgroundColor: Colors.white,
              ),
            ),
            const SizedBox(height: 32),
            Text(
              'DPP URI loaded successfully',
              style: TextStyle(
                fontSize: 14,
                color: Colors.grey[600],
              ),
            ),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                ElevatedButton.icon(
                  onPressed: () {
                    setState(() {
                      isLoading = true;
                      errorMessage = null;
                    });
                    _loadDppUri();
                  },
                  icon: const Icon(Icons.refresh),
                  label: const Text('Refresh'),
                ),
                const SizedBox(width: 12),
                ElevatedButton.icon(
                  onPressed: _clearDppUri,
                  icon: const Icon(Icons.delete),
                  label: const Text('Clear'),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.red,
                    foregroundColor: Colors.white,
                  ),
                ),
              ],
            ),
          ],
        ),
      );
    }

    return const Text('No DPP URI available');
  }
}
