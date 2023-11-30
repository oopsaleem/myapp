import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import DeptService from './dept.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IDept, Dept } from '@/shared/model/dept.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DeptUpdate',
  setup() {
    const deptService = inject('deptService', () => new DeptService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dept: Ref<IDept> = ref(new Dept());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDept = async deptId => {
      try {
        const res = await deptService().find(deptId);
        dept.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.deptId) {
      retrieveDept(route.params.deptId);
    }

    const validations = useValidation();
    const validationRules = {
      deptName: {},
      deptAddress: {},
    };
    const v$ = useVuelidate(validationRules, dept as any);
    v$.value.$validate();

    return {
      deptService,
      alertService,
      dept,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.dept.id) {
        this.deptService()
          .update(this.dept)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A Dept is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.deptService()
          .create(this.dept)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A Dept is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
