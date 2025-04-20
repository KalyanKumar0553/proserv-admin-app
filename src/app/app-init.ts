import { ConfigService } from "./shared/services/config.service";

export function initializeApp(configService: ConfigService): () => Promise<void> {
  return () => configService.loadConfig();
}
